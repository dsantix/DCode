package com.dsanti.dcode.data

import androidx.room.*

@Entity
data class QRCode(
    @PrimaryKey(autoGenerate = true) val qid: Int = 0,
    @ColumnInfo(name = "qrcode_date") val qrCodeDate: Long,
    @ColumnInfo(name = "qrcode_text") val qrCodeText: String
)

@Dao
interface QRCodeDao {
    @Query("SELECT * FROM qrcode")
    fun getAll(): List<QRCode>

    @Query("SELECT * FROM qrcode WHERE qid IN (:qrCodeIds)")
    fun loadAllByIds(qrCodeIds: IntArray): List<QRCode>

    @Insert
    fun insertAll(vararg qrCodes: QRCode)

    @Delete
    fun delete(vararg qrCodes: QRCode)

}