package serviceLayer;

import buisnessLayer.Administrator;
import buisnessLayer.Controller;
import buisnessLayer.Student;
import com.mongodb.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class DBHandler {
	public static void addAccount(String name,String DOB,String username,String password) {
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Person");
		// Adding Data
		BasicDBObject doc = new BasicDBObject("name", name).
				append("DOB", DOB).
				append("username", username).
				append("password", password).append("Type", "student");

		coll.insert(doc);
//		System.out.println("Done");
		mongoClient.close();
	}
	public static void updatePersonalInfo(String name, String DOB, String username) {
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Person");

		BasicDBObject query = new BasicDBObject();
		query.put("username", username);

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("name", name);
		newDocument.put("DOB", DOB);

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		coll.update(query, updateObj);

		mongoClient.close();
	}
	public static void updateCredentials(String username,String password,String previousUsername) {
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Person");

		BasicDBObject query = new BasicDBObject();
		query.put("username", previousUsername);

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("username", username);
		newDocument.put("password", password);

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		coll.update(query, updateObj);

		mongoClient.close();
	}
	public static Student verifyStudentLogin(String username, String password) {
		Student student = null;
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Person");

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("username", username);
		searchQuery.put("password", password);
		searchQuery.put("Type","student");

		DBCursor cursor = coll.find(searchQuery);

		if (cursor.hasNext()) {
			DBObject obj = cursor.next();
			student = new Student(obj.get("name").toString(), obj.get("DOB").toString(), obj.get("username").toString(), obj.get("password").toString());

		}

		mongoClient.close();
		return student;
	}

	public static Administrator verifyAdminLogin(String username, String password) {
		Administrator admin = null;
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Person");

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("username", username);
		searchQuery.put("password", password);
		searchQuery.put("Type","administrator");

		DBCursor cursor = coll.find(searchQuery);

		if (cursor.hasNext()) {
			DBObject obj = cursor.next();
			admin = new Administrator(obj.get("name").toString(), obj.get("DOB").toString(), obj.get("username").toString(), obj.get("password").toString());

		}

		mongoClient.close();
		return admin;
	}

	public static boolean changeFootballAvailability() {
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Bookings");

		BasicDBObject query = new BasicDBObject();
		query.put("ground", "football");

		BasicDBObject q = new BasicDBObject();
		q.put("ground", "football");

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("ground", "football");
		newDocument.put("booked", "false");

		DBCursor cur= coll.find(q);
		String str = cur.next().get("available").toString();
		if (Objects.equals(str, "true")) {
			newDocument.put("available","false");
		}
		else {
			newDocument.put("available","true");
		}

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		coll.update(query, updateObj);
		mongoClient.close();

		return true;
	}
	public static boolean changeCricketAvailability() {
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Bookings");

		BasicDBObject query = new BasicDBObject();
		query.put("ground", "cricket");

		BasicDBObject q = new BasicDBObject();
		q.put("ground", "cricket");


		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("ground", "cricket");
		newDocument.put("booked", "false");

		DBCursor cur= coll.find(q);
		String str = cur.next().get("available").toString();
		if (Objects.equals(str, "true")) {
			newDocument.put("available","false");
		}
		else {
			newDocument.put("available","true");
		}

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		coll.update(query, updateObj);
		mongoClient.close();
		return true;
	}
	public static ArrayList<String> getIssuableItems() {
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Equipment");

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("issued", "n");

		DBCursor cursor = coll.find(searchQuery);

		ArrayList<String> items = new ArrayList<String>();

		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			String data = obj.get("ref_id").toString()+" \t"+obj.get("name").toString()+" \t"+obj.get("condition").toString();
			items.add(data);
		}

		mongoClient.close();
		return items;
	}
	public static boolean checkIssuance(String rollNumber,String ref_id) {
		ArrayList<String> Issuances = new ArrayList<String>();
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Issuances");

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("roll_number",rollNumber);

		DBCursor cursor = coll.find(searchQuery);

		if (cursor.hasNext()) {
			return false;
		}

		BasicDBObject doc = new BasicDBObject("roll_number", rollNumber).
				append("ref_id", ref_id);

		coll.insert(doc);


		//===================================================
		//===================LOGS============================
		//===================================================
		DBCollection logColl = db.getCollection("Logs");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		BasicDBObject doc_2 = new BasicDBObject("rollNumber", Controller.getRollNumber()).
				append("type", "issuance").
				append("information", ref_id).
				append("time",dtf.format(now));//.append("Type", "student");

		logColl.insert(doc_2);


		//===================================================
		//===================================================
		DBCollection coll2 = db.getCollection("Equipment");

		BasicDBObject query = new BasicDBObject();
		query.put("ref_id", ref_id);

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("issued", "y");
//		newDocument.put("password", password);

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		coll2.update(query, updateObj);

		mongoClient.close();
		return true;
	}
	public static String returnIssue(String rollNumber) {
		String returnVal = null;
		ArrayList<String> Issuances = new ArrayList<String>();
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Issuances");

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("roll_number",rollNumber);

		DBCursor cursor = coll.find(searchQuery);

		if (cursor.hasNext()) {
			DBObject obj = cursor.next();
			returnVal = obj.get("ref_id").toString();
		}
		else {
			mongoClient.close();
			return null;
		}

		coll.remove(searchQuery);

		//===================================================
		//===================LOGS============================
		//===================================================
		DBCollection logColl = db.getCollection("Logs");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		BasicDBObject doc_2 = new BasicDBObject("rollNumber", Controller.getRollNumber()).
				append("type", "issuance").
				append("information", returnVal).
				append("time",dtf.format(now));//.append("Type", "student");

		logColl.insert(doc_2);

		//===================================================
		//===================================================
		DBCollection coll2 = db.getCollection("Equipment");

		BasicDBObject query = new BasicDBObject();
		query.put("ref_id", returnVal);

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("issued", "n");

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		coll2.update(query, updateObj);

		mongoClient.close();
		return returnVal;
	}
	public static String getIssue(String rollNumber){
		String returnVal = null;
		ArrayList<String> Issuances = new ArrayList<String>();
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Issuances");

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("roll_number",rollNumber);

		DBCursor cursor = coll.find(searchQuery);

		if (cursor.hasNext()) {
			DBObject obj = cursor.next();
			returnVal = obj.get("ref_id").toString();

			BasicDBObject searchQuery2 = new BasicDBObject();
			searchQuery2.put("ref_id", returnVal);

			DBCollection coll2 = db.getCollection("Equipment");
			DBCursor cursor2 = coll2.find(searchQuery2);
			DBObject obj2 = cursor2.next();
			returnVal+=",";
			returnVal+=obj2.get("name").toString();
			returnVal+=",";
			returnVal+=obj2.get("condition").toString();
		}
		mongoClient.close();
		return returnVal;
	}
	public static boolean isBooked(String sport) {
		Boolean returnVal = false;
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Bookings");

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("ground",sport);
		searchQuery.put("booked","true");
		searchQuery.put("available","true");

		DBCursor cursor = coll.find(searchQuery);

		if (cursor.hasNext()) {
			returnVal = true;
		}
		mongoClient.close();
		return returnVal;
	}
	public static boolean bookFootballGround(String rollNumber) {
		if (isBooked("football")) {
			return false;
		}
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Bookings");

		BasicDBObject query = new BasicDBObject();
		query.put("ground", "football");

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("ground", "football");
		newDocument.put("booked", "true");
		newDocument.put("available","true");

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		coll.update(query, updateObj);

//	CREATING LOGS
		DBCollection logColl = db.getCollection("Logs");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
//		System.out.println(dtf.format(now));

		BasicDBObject doc = new BasicDBObject("rollNumber", Controller.getRollNumber()).
				append("type", "booking").
				append("information", "football").
				append("time",dtf.format(now));//.append("Type", "student");

		logColl.insert(doc);
		mongoClient.close();

		return true;
	}
	public static boolean bookCricketGround(String rollNumber) {
		if (isBooked("cricket")) {
			return false;
		}
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Bookings");

		BasicDBObject query = new BasicDBObject();
		query.put("ground", "cricket");

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("ground", "cricket");
		newDocument.put("booked", "true");
		newDocument.put("available","true");

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);

		coll.update(query, updateObj);


		//	CREATING LOGS
		DBCollection logColl = db.getCollection("Logs");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		BasicDBObject doc = new BasicDBObject("rollNumber", Controller.getRollNumber()).
				append("type", "booking").
				append("information", "cricket").
				append("time",dtf.format(now));//.append("Type", "student");

		logColl.insert(doc);


		mongoClient.close();

		return true;
	}
	public static ArrayList<String> getStudentLogs(String rollNumber) {
		ArrayList<String> list = new ArrayList<String>();

		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Logs");

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("rollNumber", rollNumber);

		System.out.println("ROLL NUM: "+ rollNumber);

		DBCursor cursor = coll.find(searchQuery);

		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			String data = obj.get("rollNumber").toString()+"/"+obj.get("type").toString()+"/"+obj.get("information").toString()+"/"+obj.get("time").toString();
			list.add(data);
		}

		mongoClient.close();
//		return items;

		return list;
	}

	public static String checkFootballAvailablity() {
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Bookings");

		BasicDBObject query = new BasicDBObject();
		query.put("ground", "football");
		DBCursor cur= coll.find(query);
		mongoClient.close();
		return cur.next().get("available").toString();
	}

	public static String checkCricketAvailablity() {
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "SportEquipment" );
		DBCollection coll = db.getCollection("Bookings");

		BasicDBObject query = new BasicDBObject();
		query.put("ground", "cricket");
		DBCursor cur= coll.find(query);
		mongoClient.close();
		return cur.next().get("available").toString();
	}

	public static void main(String[] args) {
		System.out.println(getIssue("19I-0441"));
	}
}
