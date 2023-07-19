package com.zikrandmehr.firebaseandroid.main.home.storage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.ktx.storage
import com.zikrandmehr.firebaseandroid.databinding.FragmentStorageBinding

class StorageFragment : Fragment() {

    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.ivFavorite.setImageURI(it)
                imageUri = it
            }
        }
    private lateinit var ref: StorageReference
    private var userUid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = Firebase.storage.reference
        userUid = Firebase.auth.currentUser?.uid

        fetchImage()
        setupViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun fetchImage() {
        val imageRef = ref.child(String.format(SConstants.FILE_PATH_FAVORITE_IMAGE, userUid))

        imageRef.getBytes(SConstants.FILE_MAX_SIZE).addOnSuccessListener { imageData ->
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            binding.ivFavorite.setImageBitmap(bitmap)

            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                val imageUrl = downloadUri.toString()
                binding.apply {
                    grImageUrl.visibility = View.VISIBLE
                    etImageUrl.setText(imageUrl)
                }
            }
        }
    }

    private fun  setupViews() {
        binding.apply {
            toolbar.root.setNavigationOnClickListener { findNavController().navigateUp() }
            btnSelectImage.setOnClickListener { selectImage() }
            btnCopy.setOnClickListener { copyImageUrl() }
            btnUpload.setOnClickListener { uploadImage() }
        }
    }

    private fun selectImage() {
        pickImageLauncher.launch("image/*")
    }

    private fun uploadImage() {
        val imageRef = ref.child(String.format(SConstants.FILE_PATH_FAVORITE_IMAGE, userUid))

        imageUri?.let {
            imageRef.putFile(it).addOnProgressListener { (bytesTransferred, totalByteCount) ->
                val uploadProgress = (100.0 * bytesTransferred) / totalByteCount
                binding.pbUpload.apply {
                    visibility = View.VISIBLE
                    progress  = uploadProgress.toInt()
                }
            }.continueWithTask {
                imageRef.downloadUrl
            }.addOnSuccessListener {
                binding.apply {
                    pbUpload.visibility = View.GONE
                    grImageUrl.visibility = View.VISIBLE
                    etImageUrl.setText(it.toString())
                }
            }
        }
    }

    private fun copyImageUrl() {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(SConstants.IMAGE_URL, binding.etImageUrl.text)
        clipboard.setPrimaryClip(clip)
    }
}