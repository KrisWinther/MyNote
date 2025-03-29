package com.example.mynote1

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class update : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var noteId: String
    private lateinit var nameEditText: EditText
    private lateinit var desctextEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        db = FirebaseFirestore.getInstance()
        noteId = intent.getStringExtra("noteId") ?: ""

        nameEditText = findViewById(R.id.editextmain)
        desctextEditText = findViewById(R.id.editextdesc)

        loadNoteData()

        val updButton: TextView = findViewById(R.id.update)
        val deceline: TextView = findViewById(R.id.deceline)

        deceline.setOnClickListener {
            finish()
        }
        updButton.setOnClickListener {
            update()
        }
    }

    private fun loadNoteData() {
        db.collection("notes").document(noteId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    nameEditText.setText(document.getString("name"))
                    desctextEditText.setText(document.getString("desctext"))}
                else {
                    Toast.makeText(this, "Заметка не найдена", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ошибка загрузки заметки: ${e.message}", Toast.LENGTH_SHORT).show()}
    }

    private fun update() {
        val newName = nameEditText.text.toString()
        val newTextDesc = desctextEditText.text.toString()

        if (newName.isNotEmpty() && newTextDesc.isNotEmpty()) {
            val updates = hashMapOf<String, Any>()
            if (newName.isNotEmpty()) updates["name"] = newName
            if(newTextDesc.isNotEmpty()) updates["desctext"] = newTextDesc

            db.collection("notes").document(noteId)
                .update(updates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Заметка успешно обновлена!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Ошибка обновления заметки. $e", Toast.LENGTH_SHORT).show()}
        }
        else {
            Toast.makeText(this, "Пожалуйста, внесите изменения!", Toast.LENGTH_SHORT).show()
        }
    }
}