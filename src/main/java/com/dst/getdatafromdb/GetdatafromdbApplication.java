package com.dst.getdatafromdb;

import com.mongodb.client.MongoClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootApplication
public class GetdatafromdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetdatafromdbApplication.class, args);

        MongoOperations mongoOpsParsed = new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create("mongodb://parsedreader:dst-ural@localhost:27017/?authSource=wl_parsed&readPreference=primary&appname=MongoDB%20Compass&ssl=false"), "wl_parsed"));

        String machineN = "";
        String from = "";
        String to = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter machine number: ");
            machineN = br.readLine();
            System.out.println("Enter date from (yyyy-mm-dd): ");
            from = br.readLine();
            System.out.println("Enter date to (yyyy-mm-dd): ");
            to = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Instant fromInst = Instant.parse(from);
//        System.out.println(fromInst);
//        Instant toInst = Instant.parse(to).truncatedTo(ChronoUnit.MILLIS);

        String desktopPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
        String fileName = machineN.concat(" ").concat(from).concat("--").concat(to).concat(".csv");

        try (FileWriter fileWriter = new FileWriter(new File(desktopPath + "\\" + fileName))) {
//            Query query = new Query().addCriteria(Criteria.where("time").gte(fromInst).lte(toInst));
            Query query = new Query().addCriteria(Criteria.where("time").gte(from).lte(to));
//            System.out.println(query);
            fileWriter.write(mongoOpsParsed.findOne(query, ParsedEntity.class, machineN).getCsvHeader() + "\n");
            mongoOpsParsed.find(query, ParsedEntity.class, machineN).stream().map(ParsedEntity::getCsvData).forEach(s -> {
                try {
                    fileWriter.write(s + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

//            List<ParsedEntity> parsedEntity = mongoOpsParsed.find(query, ParsedEntity.class, machineN);
//            System.out.println(mongoOpsParsed.findOne(query, ParsedEntity.class, machineN).getCsvHeader());
//            mongoOpsParsed.find(query, ParsedEntity.class, machineN).stream().map(ParsedEntity::getCsvData).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
