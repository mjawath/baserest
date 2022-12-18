package com.techstart.base.rest.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.entitybase.BaseEntity;
import com.mycompany.entitybase.DataException;
import com.mycompany.entitybase.model.SearchRequest;
import com.mycompany.entitybase.model.SearchResult;
import com.mycompany.entitybase.service.IService;
import com.techstart.base.rest.controller.exceptions.BadRequest;
import com.techstart.base.rest.controller.exceptions.ContentConflict;
import com.techstart.base.rest.controller.exceptions.NoContentFound;
import com.techstart.base.rest.controller.exceptions.ServerError;
import com.techstart.commons.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class RestWSController<T extends BaseEntity> {
    private Logger logger = LoggerFactory.getLogger(RestWSController.class);
    protected IService<T> service;//will be setter injected
    protected Class busClass;
    protected static ObjectMapper om = new ObjectMapper();
    @Autowired(required = false)
    private GenericService genericService;

    public RestWSController() {
        setParameterisedBusinessClass();
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public RestWSController(IService service) {
        this();
        this.service = service;
    }

    @RequestMapping("/ping")
    protected String testRest(String txt) {
        return "rest is working: " + txt;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<T> create(@RequestBody T domainObject) {
        T resultObject = service.create(domainObject);
        return new ResponseEntity(resultObject, HttpStatus.CREATED);
    }

    @RequestMapping(value = {"/{id}", "/"}, method = RequestMethod.PUT)
    public ResponseEntity<T> update(@RequestBody(required = true) String requestBody,
                                    @PathVariable("id") String id) {

        T ob = getEntity(requestBody);
        if (ob == null) {
            throw new BadRequest("patch body cannot be empty");
        }
//        if (Objects.isNull(id)) {//conflict
//            throw new ContentConflict("id conflicts with url id and json payload");
//        }

        if (Objects.isNull(id) && Objects.isNull(ob.getId())) {
            T resultObject = service.create(ob);
            return new ResponseEntity<>(resultObject, HttpStatus.CREATED);
        }
        if (!Objects.equals(ob.getId(), id)) {//conflict
            throw new ContentConflict("id conflicts with url id and json payload");
        }
        if (Objects.isNull(ob.getId()) ) {
            ob.setId(id);
        }

        T resultObject = service.update(ob);
        return new ResponseEntity(resultObject, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<T> patch(@PathVariable("id") String id, @RequestBody String requestBody) {

        T ob = getEntity(requestBody);
        if (ob == null) {
            throw new BadRequest("patch body cannot be empty");
        }
        if (Objects.isNull(id)) {//conflict
            throw new ContentConflict("id conflicts with url id and json payload");
        }
        if (!Objects.isNull(ob.getId())
                && !Objects.equals(ob.getId(), id)) {//conflict
            throw new ContentConflict("id conflicts with url id and json payload");
        }
        return service.findById(id).map(item -> {
            try {
                T merged = om.readerForUpdating(item).readValue(requestBody);
                merged.setId(id);
                T resultObject = service.patch(merged);
                return new ResponseEntity<T>(resultObject, HttpStatus.OK);
            } catch (Exception e) {
                throw new ServerError("exception while parsing content for patch operation");
            }
        }).orElseThrow(NoContentFound::new);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        service.deleteById(id);
    }

    //    @Autowired
    public void setService(IService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<SearchResult<T>> getAll(SearchRequest request) {

        if (request == null) {
            request = new SearchRequest();
        }
        if (request.getPersistenceClass() == null) {
            request.setPersistenceClass(busClass);
        }

        SearchResult<T> search = service.search(request);
        if (search.getNumberOfElements() == 0) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(search);
    }

    @RequestMapping("/page/{pageNo}")
    public List<T> getAll(@PathVariable("pageNo") int pageNo, HttpServletRequest request) {
        return service.goToPage(pageNo);//limit
    }

    @RequestMapping("/{id}")
    public ResponseEntity<T> get(@PathVariable("id") String id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElseThrow(NoContentFound::new);
    }

    @RequestMapping("/search")
    public List<T> search(HttpServletRequest request) {
        String column = request.getParameter("column");
        String value = request.getParameter("value");

        String operator = request.getParameter("operator");
        //operator = is by default
        if (operator == null || operator.trim().length() == 0) {
            operator = "=";
        }
        System.out.println(column + "  --  " + value + "  --  " + operator + "  --  ");
        if (!StringUtil.isNullOrEmpty(column) && !StringUtil.isNullOrEmpty(value)) {
            List<T> list = service.search(column, value);
            return list;
        }
        return null;
    }

    @RequestMapping("/searchpage")
    public Page<T> searchPage(HttpServletRequest request) {
        String column = request.getParameter("column");
        String value = request.getParameter("value");
        int page = getValue(request.getParameter("page"), 0);
        int size = getValue(request.getParameter("size"), 50);

        String operator = request.getParameter("operator");
        //operator = is by default
        if (operator == null || operator.trim().length() == 0) {

            operator = "=";
        }
        System.out.println(column + "  --  " + value + "  --  " + operator + "  --  ");
        if (!StringUtil.isNullOrEmpty(column) && !StringUtil.isNullOrEmpty(value)) {
            Page<T> shops = service.searchPageable(column, value, new PageRequest(page, size));
            return shops;
        }
        return null;
    }

    protected T getEntity(String payloadJson) {
        return (T) getValue(payloadJson, busClass);
    }

    protected static <T> T getValue(String payloadJson, Class<T> busClass) {
        T ob = null;
        try {  //read JSON like DOM Parser
            JsonNode rootNode = om.readTree(payloadJson);
            JsonNode entityNode = rootNode.path("entity");
            if (entityNode != null && entityNode.isMissingNode()) {

                ob = om.readValue(rootNode.toString(), busClass);

            } else {
                ob = om.readValue(entityNode.toString(), busClass);

            }
            return ob;

        } catch (IOException ie) {
            throw new DataException("JSON Parsing Error  " + payloadJson, ie);
        }
    }

    public static <T> T getT(String payloadJson, String path, Class busClass) {
        T ob = null;
        try {  //read JSON like DOM Parser
            JsonNode rootNode = om.readTree(payloadJson);
            JsonNode entityNode = rootNode.path(path);
            return (T) om.readValue(entityNode.toString(), busClass);
        } catch (IOException ie) {
            throw new DataException("JSON Parsing Error  " + payloadJson, ie);
        }
    }

    public static Integer getValue(Object obj, int defaultValueIfNullOrEmpty) {
        if (obj == null) {
            return defaultValueIfNullOrEmpty;
        } else {

            try {
                return Integer.valueOf(obj.toString());

            } catch (Exception e) {
                throw e;
            }

        }
//    return defaultValueIfNullOrEmpty;
    }


    private void setParameterisedBusinessClass() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                busClass = ((Class) (actualTypeArguments[0]));
            }
        }
    }

}
