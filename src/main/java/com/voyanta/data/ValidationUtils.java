package com.voyanta.data;

import com.voyanta.data.utils.VHashMap;
import com.voyanta.data.utils.XMLCompareUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.io.File;
import java.util.*;

/**
 * Created by sriramangajala on 09/07/2014.
 */
public class ValidationUtils {
    static Logger LOGGER = Logger.getLogger(ValidationUtils.class);
    
    public static List<VHashMap> lowerCaseColumnsAndRemoveSpaces(List<VHashMap> data) {
        List<VHashMap> newData = new LinkedList<VHashMap>();

        for(VHashMap singleRow:data)
        {
//            Map<Integer, String> map = new TreeMap<Integer, String>(hmap);
            VHashMap newRecord = new VHashMap();
            Set set2 = singleRow.entrySet();
            Iterator iterator2 = set2.iterator();
            while(iterator2.hasNext()) {
                Map.Entry me2 = (Map.Entry)iterator2.next();
                newRecord.put(me2.getKey().toString().replace(" ","").toLowerCase(),singleRow.get(me2.getKey()));
             //   LOGGER.info("Before change....:");
               // LOGGER.info(me2.getKey() + ": ");
               // LOGGER.info(singleRow.get(me2.getKey()));
                //LOGGER.info("   After change....:");
                //LOGGER.info(me2.getKey().toString().replace(" ", "").toLowerCase() + ": ");
            }
            newData.add(newRecord);
        }
            return newData;
    }

//    public static List<HashMap> sortColumns(List<HashMap> data) {
//        for(HashMap singleRow:data)
//        {

//        }
//        return new TreeMap<String, Float>(yourMap);
//    }

    public static void compareColumnHeaders(List<VHashMap> excelSheetData, List<VHashMap> dataBaseData, String[] matchingKey) {

// int i = 0;
        int failcounter=0;
        int failedcounter=0,counter = 0;
        int totalCount = excelSheetData.size()-1;
        int recordFailCounter=0;

        for(Object key :excelSheetData.get(0).keySet())
        {
            if(!dataBaseData.get(0).containsKey(key))
            {
                LOGGER.info("Missing column in database :"+key.toString());
            }
        }

        for(int i=0;i<=totalCount;i++)
        {
// dataBaseData.get(i).humanise()
            VHashMap dbMap = dataBaseData.get(i).humanise().addExceptions();
            VHashMap excelMap = excelSheetData.get(i).addExceptions();

            if(!(excelSheetData.get(i).size()==(dbMap.size())))
            {
                LOGGER.info("Columns size not matching...");
                LOGGER.info("Excel Sheet columns are "+excelMap.size()+" where as Database columns are "+dbMap.size()+"");
                failedcounter++;

            }


//            if(!excelSheetData.get(i).keySet().equals(dataBaseData.get(i).keySet()))
//            {
//                LOGGER.info("Columns not matching");
//            }

             Set<String> set1=dbMap.keySet();
             Iterator<String> iter1=set1.iterator();

            while (iter1.hasNext())
            {
                String key = iter1.next();
                // Check if the current value is a key in the 2nd map
                if (!excelMap.containsKey(key) ){
                    LOGGER.info("This Key not available in Excel Sheet:"+key);
                    failedcounter ++;
                }
                else if (!dbMap.get(key).equals(excelMap.get(key)) )
                {
                    //ignore the "" cell coming from excel
                    if(excelMap.get(key).equals(""))
                    {
                        LOGGER.info("Ignoring the cell with no value for column:"+key.toString());
                    }
                    else {
                        LOGGER.info("Column name :" + key.toString() + " Actual Value :'" + dbMap.get(key) + "' Expected Value :'" + excelMap.get(key) + "'");
//                    LOGGER.info("Expected available:"+excelMap.get(key));
                        failedcounter++;
                    }
                }
                counter++;
            }

            LOGGER.info("TOTAL TESTS : "+counter+" record :"+(i+1));
            for(String key:matchingKey) {
                if (!key.equals(""))
                    LOGGER.info("Validating against the record found with key:" + key + " and value:" + dbMap.get(key));
            }
            if(failedcounter==0)
                LOGGER.info("NO TESTS FAILED AT DATA LEVEL VALIDATION record:"+(i+1));
            else
            {
                LOGGER.info("FAILED TESTS : "+failedcounter+" record:"+(i+1));
                failcounter++;
            }


//        Set<String> set2=excelSheetData.get(i).entrySet();
//        Iterator<String> iter2=set2.iterator();
//
//        while (iter2.hasNext())
//        {
//           String value1 = iter2.next();
//           String value2 =  ((value1).getValue();
//            // Check if the current value is a key in the 2nd map
//            if (!dataBaseData.get(i).containsValue(value1) ){
//                LOGGER.info("value not available "+value1);
//
//            }
//        }


        }
        Assert.assertEquals("Data validation failed. Please see the details above",0,failedcounter);
    }

    public static void compareRowsNumbers(List<VHashMap> excelSheetData, List<VHashMap> dataBaseData) {

        if(!(excelSheetData.size()==(dataBaseData.size())))
        {
            LOGGER.info("Row Number not matching...");
            LOGGER.info("Excel Sheet rows are :"+excelSheetData.size()+" where as Datbase rows are :"+dataBaseData.size());
        }

        for(int i=0;i<=excelSheetData.size()-1;i++)
        {
            if(excelSheetData.get(i).keySet().equals(dataBaseData.get(i).keySet()))
            {
                LOGGER.info("Both keys are Equal");
            }
            else
                LOGGER.info("Both keys are not Equal");

            if(excelSheetData.get(i).entrySet().equals(dataBaseData.get(i).entrySet()))
            {
                LOGGER.info("Both datasets are Equal");
            }
            else
                LOGGER.info("Both datasets are NOT Equal");
        }
    }

    public static void compareData(List<VHashMap> excelSheetData, List<VHashMap> dataBaseData) {
        int counter = 0;
        for(int i=0;i<=excelSheetData.size()-1;i++)
        {
            VHashMap hMap3=new VHashMap();
            Set<String> set1=excelSheetData.get(i).keySet();
            Set<String> set2=dataBaseData.get(i).keySet();

            Iterator<String> iter1=set1.iterator();
            Iterator<String> iter2=set2.iterator();
            String val="";
            while(iter1.hasNext()) {

                val=iter1.next();
             //   LOGGER.info("key and value in hmap is " + val + " " + excelSheetData.get(i).get(val));

                iter2=set2.iterator();

                while(iter2.hasNext()) {
                    String val2=iter2.next();
                   // LOGGER.info("val2 value is "+val2);

                    if(!excelSheetData.get(i).get(val).equals(dataBaseData.get(i).get(val2))) {
                     //   LOGGER.info("NO matching found for Excel " + excelSheetData.get(i).get(val) + " and Database " + dataBaseData.get(i).get(val2));
                     //   LOGGER.info("value adding");

                    }
                }
            }

        }
    }
    public static void compareListOfArrays(List<HashMap> expData, List<HashMap> actualData,String matchingKey) {

// int i = 0;
        int failcounter=0;
        int failedcounter=0,counter = 0;
        int totalCount = (actualData.size()>=expData.size())?actualData.size():expData.size();
        int recordFailCounter=0;
        String[] primaryKeys=null;

        for(Object key :expData.get(0).keySet())
        {
            if(!actualData.get(0).containsKey(key))
            {
                LOGGER.info("Missing column in Actual Data :"+key.toString());
                failcounter++;
            }
        }
        if(!(expData.size()==(actualData.size())))
        {
            LOGGER.info("number of records size not matching...");
            LOGGER.info("Expected attributes are "+expData.size()+" where as Actual attributes are "+actualData.size()+"");
            failedcounter++;
        }


        for(int i=0;i<=totalCount-1;i++)
        {

            HashMap actualRecord = getRecordWithKey(actualData,matchingKey,expData.get(i).get(matchingKey),primaryKeys);

            if(actualRecord==null)
            {
                LOGGER.info("The row with the key not found in actual data:"+expData.get(i).get(matchingKey));
                failcounter++;

            }
            else
            {
                Set<String> set1=expData.get(i).keySet();
                Iterator<String> iter1=set1.iterator();

                while (iter1.hasNext())
                {
                    String key = iter1.next();
                    // Check if the current value is a key in the 2nd map
                    if (!expData.get(i).containsKey(key) ){
                        LOGGER.info("This Key not available in expected values:"+key);
                        failedcounter ++;
                    }
                    else if (!actualRecord.equals(expData.get(i).get(key)) )
                    {
                        LOGGER.info("Column name :" + key.toString() + " Actual Value :'" + actualRecord.get(key) + "' Expected Value :'" + expData.get(i).get(key) + "'");
    //                    LOGGER.info("Expected available:"+excelMap.get(key));
                        failedcounter ++;
                    }
                    counter++;
                }
            }
            LOGGER.info("TOTAL TESTS : "+counter+" record :"+(i+1));

            if(failedcounter==0)
                LOGGER.info("NO TESTS FAILED AT DATA LEVEL VALIDATION record:"+(i+1));
            else
            {
                LOGGER.info("FAILED TESTS : "+failedcounter+" record:"+(i+1));
                failcounter++;
            }


//        Set<String> set2=excelSheetData.get(i).entrySet();
//        Iterator<String> iter2=set2.iterator();
//
//        while (iter2.hasNext())
//        {
//           String value1 = iter2.next();
//           String value2 =  ((value1).getValue();
//            // Check if the current value is a key in the 2nd map
//            if (!dataBaseData.get(i).containsValue(value1) ){
//                LOGGER.info("value not available "+value1);
//
//            }
//        }


        }
//        Assert.assertEquals("Data validation failed. Please see the details above",0,failedcounter);
    }

    private static HashMap getRecordWithKey(List<HashMap> actualData, String matchingKey, Object value,String[] primaryKeys) {

        int i=0;
        if(!actualData.get(0).containsKey(matchingKey))
        {
            LOGGER.info("The key "+matchingKey+"not found in actual data:"+value);
            return null;
        }
        for(HashMap record:actualData)
        {
            String recordValue;
            if(value==null)
            {
                if(record.get(matchingKey)==null)
                {
                    LOGGER.info("The key "+matchingKey+" found in actual data:"+value);
                    return record;
                }
              //  else
                //    return null;
            }
            else if(record.get(matchingKey)==null)
            {
                if(value==null)
                {
                    LOGGER.info("The key "+matchingKey+" found in actual data:"+value);
                    return record;
                }
               // else
                 //   return null;
            }
            //System.out.println(record.get(matchingKey) + " WITH " + value.toString());
            else if(record.get(matchingKey).toString().equals(value.toString()))
            {
                i++;
                if(isCorrectRecord(primaryKeys,record.get(matchingKey).toString(),i))
                    return record;

            }


        }

        return null;
    }

    private static boolean isCorrectRecord(String[] primaryKeys,String value, int i) {
        int occurances=getOccurances(primaryKeys,value);

        if(occurances==i)
            return true;
        return false;
    }

    private static int getOccurances(String[] primaryKeys, String value) {
       int occ=0;
        for(int i=0;i<=primaryKeys.length-1;i++)
        {
            if(primaryKeys[i]!=null) {
                if (primaryKeys[i].equals(value))
                    occ++;
            }
        }
        return occ;
    }

    public static void compareTwoThings(List<HashMap> A, List<HashMap> B, String matchingKey)
    {
        List<HashMap> biggerList=null,smallerList=null;
        int counter=0,failedcounter=0,fullFailedCounter=0;
        String[] primaryKeys=null;

        if(A.size()>B.size())
        {
            biggerList=A;
            smallerList=B;
            LOGGER.info("FAIL: Number of Records in Expected Data:"+A.size()+" doesnt match with Actual Data:"+B.size());
            fullFailedCounter++;
        }
        else if(A.size()<B.size())
        {
            biggerList=B;
            smallerList=A;
            LOGGER.info("FAIL: Number of Records in Expected Data:"+A.size()+" doesnt match with Actual Data:"+B.size());
            fullFailedCounter++;
        }
        else
        {
            biggerList=A;
            smallerList=B;
            LOGGER.info("PASS: Number of Records in Expected Data:"+A.size()+"match with Actual Data:"+B.size());
        }
        primaryKeys = new String[smallerList.size()];

        for(int i=0;i<smallerList.size();i++)
        {
            counter=0;
            failedcounter=0;
            Object value = smallerList.get(i).get(matchingKey);
            if(value!=null)
            primaryKeys[i] = value.toString();

            HashMap currentRecord = getRecordWithKey(biggerList,matchingKey,value,primaryKeys);
            if(currentRecord==null)
            {
                LOGGER.info("FAILED STEP: No record found with key:"+matchingKey+" and value:"+smallerList.get(i).get(matchingKey));
                failedcounter++;
            }
            else
            {
                LOGGER.debug("Validating against the record found with key:"+matchingKey+" and value:"+smallerList.get(i).get(matchingKey));

                counter++;

                failedcounter = compareRecords(smallerList.get(i), currentRecord);
                if(failedcounter!=0)
                    LOGGER.info("Validating against the record found with key:"+matchingKey+" and value:"+smallerList.get(i).get(matchingKey));
                counter=counter+smallerList.get(i).size();
            }

            LOGGER.debug("TOTAL TESTS : "+counter+" record :"+(i+1));

            if(failedcounter==0)
                LOGGER.debug("TESTS PASSED AT DATA LEVEL VALIDATION for record:"+(i+1));
            else
            {
                LOGGER.info("FAILED TESTS : "+failedcounter+" record:"+(i+1));

            }
            fullFailedCounter=fullFailedCounter+failedcounter;
        }

        LOGGER.info("TOTAL FAILED TESTS:"+fullFailedCounter);
        Assert.assertEquals("TESTS ARE FAILED. PLEASE SEE LOG FILE",0,fullFailedCounter);
    }

    private static int compareRecords(HashMap A, HashMap B) {

        int failedcounter=0;
        Set<String> set1=A.keySet();
        Set<String> set2=A.keySet();
        Set<String> set3=B.keySet();

        failedcounter = compareKeys(set2,set3);

        Iterator<String> iter1=set1.iterator();

        while (iter1.hasNext())
        {
            String key = iter1.next();
            // Check if the current value is a key in the 2nd map
           // if (!B.containsKey(key) ){
             //   LOGGER.info("FAILED STEP: This Key not available in expected record:"+key);
               // failedcounter ++;
            //}
            //else
            if (!A.get(key).equals(B.get(key)) )
            {
                LOGGER.info("FAILED STEP: Column name :" + key.toString() + " Actual Value :'" + A.get(key) + "' Expected Value :'" + B.get(key) + "'");
                //                    LOGGER.info("Expected available:"+excelMap.get(key));
                failedcounter ++;
            }
            //counter++;
        }
        return failedcounter;
    }

    private static int compareKeys(Set<String> set2, Set<String> set3) {
        int failures=0;
        Iterator<String> iter1=set2.iterator();
        String key;

        while (iter1.hasNext())
        {
            key = iter1.next();
            if(!set3.contains(key))
            {
                failures++;
                LOGGER.info("FAILED STEP: This Key not available in expected record:"+key);

            }
        }
        iter1=set3.iterator();

        while (iter1.hasNext())
        {
            key = iter1.next();
            if(!set2.contains(key))
            {
                failures++;
                LOGGER.info("FAILED STEP: This Key not available in expected record:"+key);
            }
        }

        return failures;

    }

    public static void compareXML(String exportExpectedFolder, String exportActualFolder) {
       XMLCompareUtils xmlCompareUtils = new XMLCompareUtils("test");
        try {
            File file1 = new File(exportExpectedFolder);
            File file2 = new File(exportActualFolder);
           xmlCompareUtils.testAllDifferences(FileUtils.readFileToString(file1, "UTF-8"),FileUtils.readFileToString(file2, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
