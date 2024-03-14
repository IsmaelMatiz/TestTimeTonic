package com.example.testtimetonic.ModelViews

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testtimetonic.Model.ApiInteraction.DTOs.Book
import com.example.testtimetonic.Model.ApiInteraction.DTOs.Contact
import com.example.testtimetonic.Model.ApiInteraction.Responses.AllBooksResponse
import com.example.testtimetonic.Model.ApiInteraction.RetroFitClient
import com.example.testtimetonic.Model.Constants
import com.example.testtimetonic.Model.Crythographer.CryptographerManager
import com.example.testtimetonic.Model.Crythographer.KeyName
import com.example.testtimetonic.Model.Crythographer.getSecretDocument
import java.io.FileInputStream

class LandingVM(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val _books = MutableLiveData<List<Book>>()

    private val _booksFilterd = MutableLiveData<List<Book>>()
    val booksFiltered : LiveData<List<Book>> = _booksFilterd

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts : LiveData<List<Contact>> = _contacts

    fun filterBooksByContactUC(contactUC: String) {
        if (contactUC == "N/A") _booksFilterd.value = _books.value
        else _booksFilterd.value = _books.value?.filter { it.contactUC == contactUC }
    }

    suspend fun getAndSetLandingInfo(oAuthUserid: String): Int{

        if (oAuthUserid == "N/A"){
            Log.e("BookStoreError","no oAuthUserid provided abort " +
                    "get and set landing")
            return 1
        }

        //fetch all books info
        try {
            var response = RetroFitClient().webServiceLanding.getAllBooks(
                Constants.GET_ALL_BOOKS_REQ.constanVal,
                oAuthUserid,
                oAuthUserid,
                getSessKey()
            ).body() as AllBooksResponse

            //set the info
            if (response.status == Constants.STATUS_OK.constanVal)
            {
                _books.value = response.allBooks?.books
                _contacts.value = response.allBooks?.contacts

                //add an option for no filter
                val noUserOpt = Contact()
                noUserOpt.userId = "N/A"
                noUserOpt.firstName = "no "
                noUserOpt.lastName = "filter"
                _contacts.value = (_contacts.value ?: emptyList()) + listOf(noUserOpt)

                //Initialy the book filter will be the books list itself
                _booksFilterd.value = _books.value
                fixePicturesLink()
            }else{
                Log.e("BookStoreError","The api answer with a nok Status")
                return 1
            }

        }catch (e : Exception){
            Log.e("BookStoreError","Erros setting the landing Info: \n$e")
            return 1
        }

        return 0

    }

    private fun getSessKey(): String {
        val file = getSecretDocument(KeyName.SECRET_KEY_SESSKEY,context)
        return CryptographerManager().decrypt(
            inputStream = FileInputStream(file),
            KeyName.SECRET_KEY_SESSKEY
        )?.decodeToString().toString()
    }

    private fun fixePicturesLink(){
        for (book in _books?.value?: emptyList()){
            if (!book.ownerPrefs?.oCoverImg.isNullOrEmpty()){
                book.ownerPrefs?.oCoverImg =
                    Constants.BASE_URL_TIMETONIC.constanVal + book.ownerPrefs?.oCoverImg
            }else{
                book.ownerPrefs?.oCoverImg = null
            }
        }
    }

}