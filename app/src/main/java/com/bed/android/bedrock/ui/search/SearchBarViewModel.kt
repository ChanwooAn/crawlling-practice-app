package com.bed.android.bedrock.ui.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.di.Injector
import com.bed.android.bedrock.model.SearchRecord
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import com.bed.android.bedrock.util.TextUtils
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class SearchBarViewModel : ViewModel() {

    private val sharedPreferences = Injector.getSharedPreferences()

    val searchKeyword = MutableLiveData("")

    private val _searchRecord = MutableLiveData<List<SearchRecord>>(emptyList())
    val searchRecord: LiveData<List<SearchRecord>>
        get() = _searchRecord

    private var popularKeywordJob: Job? = null

    fun loadSearchRecord() {
        val list = mutableListOf<SearchRecord>()

        val keywords = sharedPreferences.getString("keywords", null) ?: return

        val jsonArray = JSONArray(keywords)

        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)

            list.add(
                SearchRecord(
                    json.getInt("id"),
                    json.getString("name"),
                    json.getString("date")
                )
            )
        }

        _searchRecord.postValue(list)
    }

    suspend fun fetchPopularKeyword(callback: (List<String>) -> Unit) {
        popularKeywordJob = CoroutineScope(Dispatchers.IO).launch {
            val list = croller.croll_keywords()

            withContext(Dispatchers.Main) {
                callback(list)
            }
        }
    }

    fun addSearchKeyword(callback: (String) -> Unit) {
        searchKeyword.value?.let { keyword ->
            if (keyword.isBlank()) return

            val id = _searchRecord.value?.size ?: 0
            val record = JSONObject().apply {
                put("id", id)
                put("name", keyword)
                put("date", TextUtils.getFormattedDate())
            }

            val jsonArray = JSONArray()

            _searchRecord.value?.forEach { searchRecord ->
                JSONObject().apply {
                    put("id", searchRecord.recordId)
                    put("name", searchRecord.productName)
                    put("date", searchRecord.searchDate)
                    jsonArray.put(this)
                }
            }

            jsonArray.put(record)

            sharedPreferences.edit().apply {
                putString("keywords", jsonArray.toString())
                apply()
            }

            callback(keyword)
        }
    }

    fun onDestroyView() {
        popularKeywordJob?.cancel()
    }

    override fun onCleared() {
        popularKeywordJob?.cancel()
        super.onCleared()
    }
}
