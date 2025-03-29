package com.example.mynote1

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class read : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)

        db = FirebaseFirestore.getInstance()

        val back: TextView = findViewById(R.id.back)
        back.setOnClickListener {
            finish()
        }

        val textMain: TextView = findViewById(R.id.textmain)
        val textDesc: TextView = findViewById(R.id.textdesc)
        val id = intent.getStringExtra("id") ?: return

        db.collection("notes").document(id).get().addOnSuccessListener { document ->
            if (document != null) {
                textMain.text = document.getString("name") ?: "No Title"
                textDesc.text = document.getString("desctext") ?: "No Description"
            } else {
                Toast.makeText(this, "Заметка не существует.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Ошибка получения данных.", Toast.LENGTH_SHORT).show()
        }

        val updButton: TextView = findViewById(R.id.updater)
        updButton.setOnClickListener {
            val intent = Intent(this, update::class.java)
                .putExtra("noteId", id)
            startActivity(intent)
        }

        val deleteButton: TextView = findViewById(R.id.deliter)
        deleteButton.setOnClickListener {
            showDeleteDialog(id)
        }

        textMain.setOnLongClickListener {
            copyToClipboard(textMain.text.toString())
            true
        }

        textDesc.setOnLongClickListener {
            copyToClipboard(textDesc.text.toString())
            true
        }
    }

    private fun showDeleteDialog(documentId: String) {
        AlertDialog.Builder(this)
            .setTitle("Удаление")
            .setMessage("Вы собираетесь удалить заметку из базы данных, отментить это действие невозможно. Вы уверены, что хотите удалить заметку?")
            .setPositiveButton("Удалить") { dialog, _ ->
                db.collection("notes").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Успешно удалено.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Не удалось удалить.", Toast.LENGTH_SHORT).show()
                    }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Текст успешно скопирован.", Toast.LENGTH_SHORT).show()
    }
}
