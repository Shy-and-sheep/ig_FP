package fr.umontpellier.ig5


import org.mongodb.scala._
import org.mongodb.scala.bson.collection.mutable.Document
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.ObjectMapper
import scala.io.Source

object Project {

  def main(args: Array[String]): Unit = {

    val connectionString = "mongodb+srv://dbUser:dbUser@marvincluster.swwl8.mongodb.net/?retryWrites=true&w=majority&appName=MarvinCluster";

    val client: MongoClient = MongoClient(connectionString)

    // Database and Collection
    val database: MongoDatabase = client.getDatabase("CVE")
    val collection: MongoCollection[Document] = database.getCollection("CVE_2023")

    // JSON File Path
    val jsonFilePath = "nvdcve-1.1-2023.json"

    // Read JSON file
    val jsonSource = Source.fromFile(jsonFilePath).getLines().mkString
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    // Parse JSON to a List of Maps
    val jsonData: List[Map[String, Any]] = mapper.readValue(jsonSource, classOf[List[Map[String, Any]]])

    // Insert Documents into MongoDB
    val documents = jsonData.map(data => Document(data))
    collection.insertMany(documents).results()

    println("Data inserted successfully!")
    client.close()
  }
}
