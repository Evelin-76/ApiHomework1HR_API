import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.configurationReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


public class Homework1 {

    Response response;
    @BeforeClass
    public void beforeClass(){

        //It is coming from RestAssured
        baseURI = configurationReader.get("hr_api_url");
    }
    /*
    Q1:
- Given accept type is Json
- Path param value- US
- When users sends request to /countries
- Then status code is 200
- And Content - Type is Json
- And country_id is US
- And Country_name is United States of America
- And Region_id is
     */
    @Test
    public void Q1(){
        response = given().accept(ContentType.JSON)
                .and().pathParam("value","US")
                .when().get("/countries/{value}");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");
        assertEquals(response.path("country_id"), "US");
        assertEquals(response.path("country_name"), "United States of America");
        int regionId =response.path("region_id") ;
        assertEquals(regionId,2);
    }
    /*
    Q2:
- Given accept type is Json
- Query param value - q={"department_id":80}
- When users sends request to /employees
- Then status code is 200
- And Content - Type is Json
- And all job_ids start with 'SA'
- And all department_ids are 80
- Count is 25
     */

    @Test
    public void Q2(){

        response = given().accept(ContentType.JSON)
                .and().queryParams("q","{\"department_id\":80}")
                .when().get("/employees");
      //  JsonPath jsonPath = response.jsonPath();
        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");

        List<String> jobIdSA = response.path("items.job_id");
        for (String eachjobId : jobIdSA) {
         //   System.out.println(eachjobId);
            assertTrue(eachjobId.startsWith("SA"));
        }

        List<Integer> departID = response.path("items.department_id");
        for (Integer eachDepart : departID) {
        //    System.out.println(eachDepart);
            int each = eachDepart;
            assertEquals(each,80);
        }
        int count = response.path("count");
        assertEquals(count,25);
    }
    /*
    Q3:
- Given accept type is Json
-Query param value q= region_id 3
- When users sends request to /countries
- Then status code is 200
- And all regions_id is 3
- And count is 6
- And hasMore is false
- And Country_name are;
Australia,China,India,Japan,Malaysia,Singapore
     */
    @Test
    public void Q3(){
        response = given().accept(ContentType.JSON)
                .and().queryParams("q","{\"region_id\":3}")
                .when().get("/countries");

        assertEquals(response.statusCode(),200);

       List<Integer> regionsId = response.path("items.region_id");
        List<Integer> regionId = response.path("items.region_id");
        for (int eachRegionId : regionId) {
            assertEquals(eachRegionId,3);
        }

        int count = response.path("count");
        assertEquals(count,6);

        boolean hasMore = response.path("hasMore");
        assertEquals(hasMore,false);

        List<String> countryNames = response.path("items.country_name");
        List<String> expectCountryNames = new ArrayList<>();
        String[] list = {"Australia", "China", "India", "Japan", "Malaysia", "Singapore"};
        Collections.addAll(expectCountryNames,list);
//        System.out.println(expectCountryNames);
//        System.out.println(countryNames);
        assertEquals(countryNames,expectCountryNames);
    }
}
