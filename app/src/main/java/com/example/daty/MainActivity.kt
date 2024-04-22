package com.example.daty

import android.widget.TextView
import android.widget.AdapterView
import android.view.View
import android.widget.Button
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Build
import android.os.Environment
import android.util.Log // Added import statement for Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daty.Data // Assuming Data class exists (or remove if not used)
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.LocalDate
import java.util.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import android.widget.ArrayAdapter
import android.widget.Spinner

// Data class definition
class Data(val name: String, val description: String, val date: LocalDate, val index: Int, val month: Int)

class MainActivity : AppCompatActivity() {

    // Initialize adapter as a field of MainActivity
    private lateinit var adapter: ProductAdapter

    // Initialize spinner adapter as a field of MainActivity
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    // Initialize RecyclerView, TextView, and Spinner as fields of MainActivity
    private lateinit var recyclerView: RecyclerView
    private lateinit var indexTextView: TextView
    private lateinit var monthSpinner: Spinner

    // Initialize current index and month variables
    private var currentIndex = 1
    private var currentMonth = 1

    // Initialize dataList as a field of MainActivity
    private var dataList: List<Data> = listOf()

    // Define storage permission request code
    private val STORAGE_PERMISSION_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize indexTextView with the corresponding view in the layout
        indexTextView = findViewById(R.id.indexTextView)

        // Check and request storage permission if needed
        checkStoragePermission()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        } else {
            // Permission already granted, write to the file
            writeDataToCSV()
        }

        // Initialize adapter with dataList and set it to the RecyclerView
        adapter = ProductAdapter(this, dataList)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Initialize spinner adapter with months array and set it to the Spinner
        val months = arrayOf("Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień")
        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthSpinner = findViewById<Spinner>(R.id.monthSpinner)
        monthSpinner.adapter = spinnerAdapter

        // Set onItemSelectedListener for the Spinner
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentMonth = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Add click listener for the addButton
        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            if (indexTextView.text.isNotEmpty() && indexTextView.text.isNotBlank()) {
                currentIndex = indexTextView.text.toString().toInt()
            }
            val newData = Data("Nowy element", "Opis nowego elementu", LocalDate.now(), currentIndex, currentMonth)
            addItem(newData)
            writeDataToCSV()
        }

        // Add click listener for the removeButton
        val removeButton = findViewById<Button>(R.id.removeButton)
        removeButton.setOnClickListener {
            if (indexTextView.text.isNotEmpty() && indexTextView.text.isNotBlank()) {
                val indexToRemove = indexTextView.text.toString().toInt()
                removeItem(indexToRemove)
            }
        }
    }

    // Check and request storage permission if needed
    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
    }

    // Add a new item to the dataList and update the RecyclerView
    private fun addItem(data: Data) {
        if (dataList.isNotEmpty()) {
            dataList = dataList.plus(data)
            adapter.updateDataList(dataList)
            writeDataToCSV()
        } else {
            // Handle the case when the list is empty
            // For example, initialize the list with the new item
            dataList = listOf(data)
            adapter.updateDataList(dataList)
            writeDataToCSV()
        }
    }

    // Remove an item from the dataList based on the given index and update the RecyclerView
    private fun removeItem(index: Int) {
        if (dataList.isNotEmpty() && index >= 0 && index < dataList.size) {
            dataList = dataList.filter { it.index!= index }
            adapter.updateDataList(dataList)
            writeDataToCSV()
        }
    }

    // Write dataList to CSV file
    private fun writeDataToCSV() {
        Log.d("daty", "Writing data to CSV")
        val file = File(Environment.getExternalStorageDirectory().toString() + "/daty.csv")
        try {
            val writer = FileWriter(file, false)
            val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("name", "description", "date", "index", "month"))
            for (data in dataList) {
                csvPrinter.printRecord(data.name, data.description, data.date, data.index, data.month)
            }
            csvPrinter.flush()
            csvPrinter.close()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}