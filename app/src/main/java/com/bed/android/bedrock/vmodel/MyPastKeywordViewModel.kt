package com.bed.android.bedrock.vmodel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.model.SearchRecord

class MyPastKeywordViewModel:ViewModel() {
    private val records = mutableListOf<SearchRecord>()
    var keyAndDate = MutableLiveData<List<SearchRecord>>()

    fun addRecord(record: SearchRecord) {
        var found = false
        var f_idx = 0
        for (i in records.indices) {
            if (records[i].productName == record.productName) {
                found = true
                f_idx = i
                break
            }
        }

        if (found) {
            records.set(f_idx, record)
        }
        else{
            records.add(record)
        }
        keyAndDate.value = records
    }

    fun removeRecord(record: SearchRecord) {
        records.remove(record)
        keyAndDate.value = records
    }
}