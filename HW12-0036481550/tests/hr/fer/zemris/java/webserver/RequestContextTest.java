package hr.fer.zemris.java.webserver;

import java.util.List;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class RequestContextTest {

    private RequestContext rc;

    @Before
    public void init() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "Karlo");
        parameters.put("surname", "Vrbic");

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        rc = new RequestContext(os, parameters, null, null);

        rc.setPersistentParameter("name", "Karlo");
        rc.setPersistentParameter("surname", "Vrbic");
        rc.setTemporaryParameter("temp1", "1");
        rc.setTemporaryParameter("temp2", "2");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor1() {
        new RequestContext(null, null, null, null);
    }

    @Test
    public void testConstructor2() {
        new RequestContext(System.out, null, null, null);
    }

    @Test
    public void testConstructor3() {
        new RequestContext(System.out, new HashMap<String, String>(), null, null);
    }

    @Test
    public void testConstructor4() {
        new RequestContext(System.out, new HashMap<String, String>(), new HashMap<String, String>(), null);
    }

    @Test
    public void testConstructor5() {
        new RequestContext(System.out, new HashMap<String, String>(), new HashMap<String, String>(), new ArrayList<>());
    }

    @Test
    public void testGetParameter() {
        Assert.assertEquals("Karlo", rc.getParameter("name"));
        Assert.assertEquals("Vrbic", rc.getParameter("surname"));
    }

    @Test
    public void testGetPParameter() {
        Assert.assertEquals("Karlo", rc.getPersistentParameter("name"));
        Assert.assertEquals("Vrbic", rc.getPersistentParameter("surname"));
    }
    
    @Test
    public void testGetTParameter() {
        Assert.assertEquals("1", rc.getTemporaryParameter("temp1"));
        Assert.assertEquals("2", rc.getTemporaryParameter("temp2"));
    }
    
    @Test
    public void testGetPParameters() {
        List<String> expected = new ArrayList<>();
        expected.add("name");
        expected.add("surname");
        
        Assert.assertArrayEquals(expected.toArray(), rc.getPersistentParameterNames().toArray());
    }
    
    @Test
    public void testGetTParameters() {
        List<String> expected = new ArrayList<>();
        expected.add("temp1");
        expected.add("temp2");
        
        Assert.assertArrayEquals(expected.toArray(), rc.getTemporaryParameterNames().toArray());
    }
}
