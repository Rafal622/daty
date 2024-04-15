package com.example.daty

import java.io.FileOutputStream
import android.os.Bundle
import java.time.LocalDate
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
// import com.example.daty.Product

class Data(val name: String, val description: String, val date: LocalDate)  // Add Data class definition (if needed)

class MainActivity : AppCompatActivity() {

    private fun writeDataToCSV() {
        val filename = "Daty.csv"
        val fileOutputStream: FileOutputStream? = try {
            openFileOutput(filename, Context.MODE_PRIVATE)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        val csvWriter = CSVWriter(fileOutputStream)
        val header = listOf("Nazwa", "Opis", "Data")
        csvWriter.write(header)
        csvWriter.write(dataList.map { it.toList() })
        csvWriter.close()
    }

    private var dataList: MutableList<Data> = mutableListOf()

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val products = emptyList<Product>()

        val adapter = ProductAdapter(this, products)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val addButton = findViewById<Button>(R.id.addButton) // Assuming you have a button with id "addButton"
        val removeButton = findViewById<Button>(R.id.removeButton) // Assuming you have a button with id "removeButton"

        addButton.setOnClickListener {
            // Dodaj nowy element do listy
            val newData = Data("Nowy element", "Opis nowego elementu", LocalDate.now())
            addItem(newData)
            writeDataToCSV() // Zapisz dane do pliku CSV
        }

        removeButton.setOnClickListener {
            // Usuń element z listy
            if (dataList.isNotEmpty()) {
                removeItem(dataList.last())
            }
        }
    }
    // **Step 3: Implement addItem() method to add new elements**
    fun addItem(data: Data) {
        dataList.add(data)
       //  adapter.notifyDataSetChanged() // Notify adapter about data change
    }

    // **Step 5: Implement removeItem() method to remove elements**
    fun removeItem(data: Data) {
        if (dataList.contains(data)) {
            dataList.remove(data)
        //    adapter.notifyDataSetChanged() // Notify adapter about data change
        }
    }


}