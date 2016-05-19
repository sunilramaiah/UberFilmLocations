package org.sunil.ufl.jest;

import io.searchbox.client.JestClient;
import io.searchbox.indices.IndicesExists;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Helper Util class.
 * 
 * @author sunil
 *
 */
public class JestHelper {
	/**
	 * 
	 * This check if the given index by name exists and returns true/false.
	 * 
	 * @param client
	 * @param name
	 * @return boolean
	 * @throws Exception
	 */
    public static boolean indexExists(JestClient client, String name) throws Exception {
        return client.execute(new IndicesExists.Builder(name).build()).isSucceeded();
    }
    
    /**
     * This will return the schema string for the index, with the given name.
     * 
     * @param schemName
     * @return String
     * @throws Exception
     */
    public static String getSchemaAsString(String schemName) throws Exception {
        
        BufferedReader br = new BufferedReader(new FileReader(new File(JestHelper.class.getClassLoader().getResource(schemName + ".json").getFile())));
        StringBuffer buffer = new StringBuffer();
        String temp = null;
        
        while ((temp = br.readLine()) != null) {
            buffer.append(temp).append("\n");
        }
        
        br.close();
        
        return buffer.toString();
    }
}
