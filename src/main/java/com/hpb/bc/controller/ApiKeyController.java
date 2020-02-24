package com.hpb.bc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hpb.bc.entity.ApiKey;
import com.hpb.bc.entity.apiKey.ApikeyAction;
import com.hpb.bc.entity.apiKey.ApikeyCreate;
import com.hpb.bc.entity.apiKey.ApikeyUpdate;
import com.hpb.bc.entity.apiKey.View;
import com.hpb.bc.exception.ApikeyException;
import com.hpb.bc.service.ApiKeyService;
import com.hpb.bc.service.MailService;
import com.hpb.bc.util.apiKey.ApiName;
import com.hpb.bc.util.apiKey.PassGenerator;
import com.hpb.bc.util.apiKey.Tools;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;


@RestController
@RequestMapping("/apikey")
public class ApiKeyController extends BaseController {

    private static final String READ = "read";
    private static final String WRITE = "write";
    private static final Logger LOG = LogManager.getLogger(ApiKeyController.class);
    private static final String MISSINGPARAMETER = "missing parameter";
    private static final String APIKEYNOTFOUND = "Apikey-not-found";
    private static final String APIKEYDEPRECATED = "apikey {} is deprecated";
    @Autowired
    public MailService mailService;
    @Autowired
    public SimpleMailMessage simpleMailMessage;
    @Autowired
    private ApiKeyService apiKeyService;

    @CrossOrigin(maxAge = 600)
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> save(@RequestBody ApikeyCreate apikeyCreate) {

        LOG.debug("creating new apikey");
        String missing = mandatoryMissing(apikeyCreate);
        if (!missing.equals("")) {
            LOG.debug(missing + ", abort creating apikey");
            return new ResponseEntity<>(new ApikeyException(400, MISSINGPARAMETER, missing), HttpStatus.BAD_REQUEST);
        }

        PassGenerator pg = new PassGenerator();
        String newApiKey;
        do {
            newApiKey = pg.generate(RandomUtils.nextInt(8, 13));
        } while (null != this.apiKeyService.findApiKeyByApiKey(newApiKey));

        ApiKey apikey = new ApiKey(newApiKey,
                Tools.generatePassPhrase(10),
                apikeyCreate.getFirstName(),
                apikeyCreate.getLastName(),
                apikeyCreate.getEmail()
        );
        if (null != apikeyCreate.getWebsite()) {
            apikey.setWebsite(apikeyCreate.getWebsite());
        }
        if (null != apikeyCreate.getAppName()) {
            apikey.setAppName(apikeyCreate.getAppName());
        }
        if (null != apikeyCreate.getCompany()) {
            apikey.setCompany(apikeyCreate.getCompany());
        }
        if (null != apikeyCreate.getSector()) {
            apikey.setSector(apikeyCreate.getSector());
        }
        this.apiKeyService.save(apikey);
        LOG.debug("apikey: {} created", apikey.getApiKey());

        mailService.sendSimpleMessageUsingTemplate(apikey.getEmail(),
                "Your HPB API keys",
                simpleMailMessage,
                apikey.getFirstName(),
                apikey.getLastName(),
                apikey.getApiKey(),
                apikey.getPrivateKey());
        return new ResponseEntity<>(apikey, HttpStatus.CREATED);
    }


    @CrossOrigin(maxAge = 600)
    @RequestMapping(method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@RequestBody ApikeyUpdate apikeyUpdate) {
        LOG.debug("updateMoreInfo registration details for apikey: {}", apikeyUpdate.getApikey());
        String missing = mandatoryMissing(apikeyUpdate);
        if (!missing.equals("")) {
            LOG.debug(missing + ", aborting registration details updateMoreInfo");
            return new ResponseEntity<>(new ApikeyException(400, MISSINGPARAMETER, missing), HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();


        ApiKey apikey = this.apiKeyService.findApiKeyByApiKey(apikeyUpdate.getApikey());
        if (null == apikey) {
            LOG.debug("apikey: {} not found", apikeyUpdate.getApikey());
            headers.add(APIKEYNOTFOUND, APIKEYNOTFOUND.toLowerCase());
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        } else {
            LOG.debug("updateMoreInfo registration details for apikey: {}", apikey.getApiKey());
        }


        if (null != apikey.getDeprecationDate() && apikey.getDeprecationDate().before(new Date())) {
            LOG.debug(APIKEYDEPRECATED, apikeyUpdate.getApikey());
            return new ResponseEntity<>(HttpStatus.GONE);
        }
        apikey = copyUpdateValues(apikey, apikeyUpdate);
        this.apiKeyService.save(apikey);
        return new ResponseEntity<>(apikey, headers, HttpStatus.OK);
    }


    @RequestMapping(path = "/{id}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> reenable(@PathVariable("id") String id,
                                           @RequestBody(required = false) ApikeyUpdate apikeyUpdate) {
        LOG.debug("re-enable invalidated apikey: {}", id);
        HttpHeaders headers = new HttpHeaders();


        ApiKey apikey = this.apiKeyService.findApiKeyById(id);
        if (null == apikey) {
            LOG.debug(APIKEYNOTFOUND + " with value: " + id);
            headers.add(APIKEYNOTFOUND, APIKEYNOTFOUND.toLowerCase());
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }

        apikey.setDeprecationDate(null);


        if (null != apikeyUpdate) {
            String missing = mandatoryMissing(apikeyUpdate);
            if (!missing.equals("")) {
                return new ResponseEntity<>(new ApikeyException(400, MISSINGPARAMETER, missing), HttpStatus.BAD_REQUEST);
            }
            apikey = copyUpdateValues(apikey, apikeyUpdate);
        }
        this.apiKeyService.save(apikey);
        return new ResponseEntity<>(apikey, headers, HttpStatus.OK);
    }


    @CrossOrigin(maxAge = 600)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        LOG.debug("invalidate apikey: {}", id);
        ApiKey apikey = this.apiKeyService.findApiKeyById(id);
        HttpHeaders headers = new HttpHeaders();


        if (null == apikey) {
            LOG.debug(APIKEYNOTFOUND + " with value: " + id);
            headers.add(APIKEYNOTFOUND, APIKEYNOTFOUND.toLowerCase());
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }


        if (null != apikey.getDeprecationDate() && apikey.getDeprecationDate().before(new Date())) {
            LOG.debug(APIKEYDEPRECATED, id);
            return new ResponseEntity<>(HttpStatus.GONE);
        }

        apikey.setDeprecationDate(new DateTime(DateTimeZone.UTC).toDate());
        this.apiKeyService.save(apikey);
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }


    @CrossOrigin(maxAge = 600)
    @JsonView(View.Public.class)
    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> get(@PathVariable("id") String id) {
        LOG.debug("retrieve details for apikey: {}", id);
        HttpHeaders headers = new HttpHeaders();
        ApiKey apikey = this.apiKeyService.findApiKeyById(id);
        if (null == apikey) {
            LOG.debug(APIKEYNOTFOUND + " with value: " + id);
            headers.add(APIKEYNOTFOUND, APIKEYNOTFOUND.toLowerCase());
            return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(apikey, headers, HttpStatus.OK);
    }


    @RequestMapping(path = "/{id}/validate", method = RequestMethod.POST)
    public ResponseEntity<Object> validate(@PathVariable("id") String id,
                                           @RequestParam(value = "api", required = false) String api,
                                           @RequestParam(value = "method", required = false) String method) {

        LOG.debug("validate apikey: {}", id);
        HttpHeaders headers = new HttpHeaders();
        DateTime nowDtUtc = new DateTime(DateTimeZone.UTC);
        Date now = nowDtUtc.toDate();

        if (StringUtils.isEmpty(api)) {
            LOG.debug("no value for parameter 'api' supplied");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!EnumUtils.isValidEnum(ApiName.class, api.toUpperCase())) {
            LOG.debug("illegal value for parameter 'api': {}", api);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isEmpty(method)) {
            LOG.debug("no value for parameter 'method' supplied");
        } else if (method.equalsIgnoreCase("krakboem")) {
            try {
                int oops = 0 / 0;
            } catch (ArithmeticException e) {
                LOG.error("Deliberate error thrown: ", e);
            }
        } else if (!method.equalsIgnoreCase(READ) && !method.equalsIgnoreCase(WRITE)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        ApiKey apikey = this.apiKeyService.findApiKeyById(id);
        if (null == apikey) {
            LOG.debug(APIKEYNOTFOUND + " with value: " + id);
            headers.add(APIKEYNOTFOUND, APIKEYNOTFOUND.toLowerCase());
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }


        if (null != apikey.getDeprecationDate() && apikey.getDeprecationDate().before(new Date())) {
            LOG.debug(APIKEYDEPRECATED, id);
            return new ResponseEntity<>(HttpStatus.GONE);
        }


        if (null == apikey.getActivationDate()) {
            apikey.setActivationDate(now);
        }


        long usage = apikey.getUsageNumber();
        long remaining = apikey.getUsageLimit() - usage;
        headers.add("X-RateLimit-Reset",
                String.valueOf(new Duration(nowDtUtc,
                        nowDtUtc.plusDays(1).withTimeAtStartOfDay()).toStandardSeconds()
                        .getSeconds()));

        if (remaining <= 0L) {

            headers.add("X-RateLimit-Remaining", String.valueOf(0));
            LOG.debug("usage limit of apikey {} reached", id);
            return new ResponseEntity<>(headers, HttpStatus.TOO_MANY_REQUESTS);
        } else {

            headers.add("X-RateLimit-Remaining", String.valueOf(remaining - 1));
            apikey.setUsageNumber(usage + 1);
            this.apiKeyService.save(apikey);
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        }
    }


    @RequestMapping(path = "/{id}/set", method = RequestMethod.PUT)
    public ResponseEntity<Object> validate(@PathVariable("id") String id,
                                           @RequestParam(value = "limit", required = false) Long limit,
                                           @RequestParam(value = "reset", required = false) Boolean reset,
                                           @RequestParam(value = "deprecated", required = false) Boolean deprecated) {

        Date lastWeek = new DateTime(DateTimeZone.UTC).minusDays(7).toDate();

        ApiKey apikey = this.apiKeyService.findApiKeyById(id);
        if (null == apikey) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (null != reset && reset) {
            apikey.setUsageNumber(0L);
        }

        if (null != limit) {
            apikey.setUsageLimit(limit);
        }

        if (BooleanUtils.isTrue(deprecated)) {
            apikey.setDeprecationDate(lastWeek);
        } else if (BooleanUtils.isFalse(deprecated)) {
            apikey.setDeprecationDate(null);
        }

        if (null == reset && null == deprecated && null == limit) {

            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        } else {
            this.apiKeyService.findApiKeyByApiKey("");

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }


    @RequestMapping(path = "", method = RequestMethod.GET)
    public String helloWorld() {
        LOG.debug("hello world endpoint");
        return "Hello World!";
    }

    private ApiKey copyUpdateValues(ApiKey apikey, ApikeyUpdate apikeyUpdate) {
        if (null != apikeyUpdate.getFirstName()) {
            apikey.setFirstName(apikeyUpdate.getFirstName());
        }
        if (null != apikeyUpdate.getLastName()) {
            apikey.setLastName(apikeyUpdate.getLastName());
        }
        if (null != apikeyUpdate.getEmail()) {
            apikey.setEmail(apikeyUpdate.getEmail());
        }
        if (null != apikeyUpdate.getWebsite()) {
            apikey.setWebsite(apikeyUpdate.getWebsite());
        }
        if (null != apikeyUpdate.getAppName()) {
            apikey.setAppName(apikeyUpdate.getAppName());
        }
        if (null != apikeyUpdate.getCompany()) {
            apikey.setCompany(apikeyUpdate.getCompany());
        }
        if (null != apikeyUpdate.getSector()) {
            apikey.setSector(apikeyUpdate.getSector());
        }
        return apikey;
    }

    private String mandatoryMissing(ApikeyAction apikeyUpdate) {
        String retval = "required parameter";
        ArrayList<String> missingList = new ArrayList<>();
        if (null == apikeyUpdate.getFirstName()) {
            missingList.add("'firstName'");
        }
        if (null == apikeyUpdate.getLastName()) {
            missingList.add("'lastName'");
        }
        if (null == apikeyUpdate.getEmail()) {
            missingList.add("'email'");
        }

        if (missingList.size() == 3) {
            retval += "s " + missingList.get(0) + ", " + missingList.get(1) + " and " + missingList.get(2);
        } else if (missingList.size() == 2) {
            retval += "s " + missingList.get(0) + " and " + missingList.get(1);
        } else if (missingList.size() == 1) {
            retval += " " + missingList.get(0);
        } else {
            return "";
        }
        return retval + " not provided";
    }


}
