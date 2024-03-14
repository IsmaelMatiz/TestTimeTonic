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

/**
 * ViewModel class responsible for handling book and contact data for the landing screen.
 *
 * This class fetches book and contact information from the Timetonic API and exposes them
 * through LiveDatas for observation by the UI layer. It also handles filtering books based
 * on the selected contact.
 */
class LandingVM(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val _books = MutableLiveData<List<Book>>()

    private val _booksFilterd = MutableLiveData<List<Book>>()
    val booksFiltered : LiveData<List<Book>> = _booksFilterd

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts : LiveData<List<Contact>> = _contacts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    /**
     * Filters the books based on a given contact UC.
     *
     * @param contactUC The contact UC to filter by.
     */
    fun filterBooksByContactUC(contactUC: String) {
        if (contactUC == "N/A") _booksFilterd.value = _books.value
        else _booksFilterd.value = _books.value?.filter { it.contactUC == contactUC }
    }

    /**
     * Fetches and sets landing information(books and contacts) from the API.
     *
     * @param oAuthUserid The OAuth user ID for authentication.
     * @return 0 for success, 1 for errors.
     */
    suspend fun getAndSetLandingInfo(oAuthUserid: String): Int{
        _isLoading.value = true
        if (oAuthUserid == "N/A"){
            Log.e("BookStoreError","no oAuthUserid provided abort " +
                    "get and set landing")

            _isLoading.value = false
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
                _isLoading.value = false
                return 1
            }

        }catch (e : Exception){
            Log.e("BookStoreError","Erros setting the landing Info: \n$e")
            _isLoading.value = false
            return 1
        }

        _isLoading.value = false
        return 0

    }

    /**
     * Retrieves the session key and decrypt it from file.
     *
     * @return The decrypted session key.
     */
    private fun getSessKey(): String {
        val file = getSecretDocument(KeyName.SECRET_KEY_SESSKEY,context)
        return CryptographerManager().decrypt(
            inputStream = FileInputStream(file),
            KeyName.SECRET_KEY_SESSKEY
        )?.decodeToString().toString()
    }

    /**
     * Fixes the image URLs for book covers.
     *
     * This function iterates  adds the [Constants.BASE_URL_TIMETONIC]
     * through the list of books stored in [_books] and checks if the
     * `oCoverImg` property within the `ownerPrefs` object is not null or empty.
     */
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