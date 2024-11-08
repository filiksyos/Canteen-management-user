package com.canteenmanagment.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import com.canteenmanagment.R
import com.canteenmanagment.databinding.OrderDetailDiaologBoxBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class OrderDetailDialog(val activity: Activity) {

    lateinit var alertDialog: Dialog
    private lateinit var binding: OrderDetailDiaologBoxBinding

    fun startDialog(orderId: String, transactionId: String) {
        val dialog = AlertDialog.Builder(activity)

        // Initialize ViewBinding
        binding = OrderDetailDiaologBoxBinding.inflate(LayoutInflater.from(activity))
        val view = binding.root

        dialog.setView(view)

        // Set transaction ID
        binding.TVTransactionId.text = "Transaction id : $transactionId"

        // Generate QR code using Zxing
        try {
            val bitmap = generateQRCode(orderId)
            binding.IMQrCode.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Log.e("QR Code Error:", e.toString())
        }

        // Show dialog
        alertDialog = dialog.create()
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun stopDialog() {
        alertDialog.dismiss()
    }

    // Function to generate QR code using Zxing
    private fun generateQRCode(content: String): Bitmap? {
        val size = 900 // specify the desired size of the QR code
        val qrCodeWriter = QRCodeWriter()
        return try {
            val bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bmp
        } catch (e: WriterException) {
            Log.e("QR Code Generation", "Error generating QR code", e)
            null
        }
    }
}
