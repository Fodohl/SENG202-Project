package seng202.team1.unittests.helpers;

import org.junit.jupiter.api.Test;
import seng202.team1.helpers.URLHelpers;
import seng202.team1.unittests.TrashAppTest;

import java.net.URI;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class URLHelpersTest extends TrashAppTest {

     @Test
    void getPageGivenDirShouldBeEmpty(){
        URI uri = URI.create("http://localhost/route/");
        assertEquals("", URLHelpers.getPage(uri));
    }

     @Test
    void getPageShouldGetFullPageName(){
        URI uri = URI.create("http://localhost/api/userinfo");
        assertEquals("userinfo", URLHelpers.getPage(uri));
    }

     @Test
    void shouldSaftleyGetWithNoParams(){
        URI uri = URI.create("http://localhost/api/userinfo");
        assertEquals(0,  URLHelpers.getParams(uri).size());
    }

     @Test
    void urlParamsShouldRemoveInteralKeys(){
        URI uri = URI.create("http://localhost/api/userinfo?key=value&_=test");
        HashMap<String, String> params = URLHelpers.getParams(uri);

        assertFalse(params.containsKey("_"));
        assertEquals("value", params.get("key"));
    }

    @Test
    void getParamsShouldParseMultipleQueryParameters(){
        URI uri = URI.create("http://localhost/api/userinfo?q=test&limit=1");
        HashMap<String, String> params = URLHelpers.getParams(uri);

        assertEquals(2, params.size());
        assertEquals("test", params.get("q"));
        assertEquals("1", params.get("limit"));
    }

}