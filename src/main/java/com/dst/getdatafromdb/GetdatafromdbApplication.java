package com.dst.getdatafromdb;

import com.mongodb.client.MongoClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.*;

@SpringBootApplication
public class GetdatafromdbApplication {

    private static final Logger logger = LogManager.getLogger(GetdatafromdbApplication.class);

    public static void main(String[] args) {
//        SpringApplication.run(GetdatafromdbApplication.class, args);

//        MongoOperations mongoOpsParsed = new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create("mongodb://parsedreader:dst-ural@192.168.211.28:27017/?authSource=wl_parsed&readPreference=primary&appname=MongoDB%20Compass&ssl=false"), "wl_parsed"));

        String machineN = "";
        String from = "";
        String to = "";
//        String ipServ = "192.168.";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
//            System.out.println("Enter server ip: 192.168.? (example \"211.7\")");
//            ipServ = ipServ.concat(br.readLine());
            System.out.println("Enter machine number: ");
            machineN = br.readLine();
            System.out.println("Enter date from (yyyy-mm-dd): ");
            from = br.readLine();
            System.out.println("Enter date to (yyyy-mm-dd): ");
            to = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MongoOperations mongoOpsParsed = new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create("mongodb://parsedreader:dst-ural@192.168.210.235:27017/?authSource=wl_parsed&readPreference=primary&appname=MongoDB%20Compass&ssl=false"), "wl_parsed"));

        String fileName = machineN.concat(" ").concat(from).concat("--").concat(to).concat(".csv");

        from = from.concat("T00:00:000Z");
        to = to.concat("T23:59:599Z");

        Query query = new Query().addCriteria(Criteria.where("time").gte(from).lte(to));
        ParsedEntity parsedEntity = mongoOpsParsed.findOne(query, ParsedEntity.class, machineN);
        if (parsedEntity != null) {
            File dir = new File("locarus_data");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outputFile = new File(dir.getAbsolutePath() + "\\" + fileName);
            try (FileWriter fileWriter = new FileWriter(outputFile)) {
                String header = parsedEntity.getCsvHeader();
                fileWriter.write(header + "\n");
                mongoOpsParsed.find(query, ParsedEntity.class, machineN).stream().map(ParsedEntity::getCsvData).forEach(s -> {
                    try {
                        fileWriter.write(s + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println("Created file: " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("No data");
        }
    }

}
