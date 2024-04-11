package com.lutech.videodownloader.scenes

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.lutech.videodownloader.BuildConfig
import com.lutech.videodownloader.R
import com.lutech.videodownloader.callback.ItemClickListener
import com.lutech.videodownloader.model.VideoFile
import com.lutech.videodownloader.utils.Constants
import com.lutech.videodownloader.utils.Utils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_video_downloaded.view.*
import kotlinx.android.synthetic.main.item_video_downloaded.view.btnShare
import kotlinx.android.synthetic.main.item_video_downloaded.view.ivVideoImage
import kotlinx.android.synthetic.main.item_video_downloaded.view.tvTimeLengthOfVideo
import kotlinx.android.synthetic.main.item_video_downloaded2.view.*
import java.io.File


class VideoDownloadedAdapter2(
    var context: Context,
    var listVideoDownloadedAdapter: List<VideoFile>? = null,
    var listener: ItemClickListener
) : RecyclerView.Adapter<VideoDownloadedAdapter2.MyViewHolder>() {
    private var dialogDeleteFile: Dialog? = null
    private var dialogMore: Dialog? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        initData()
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_video_downloaded2, parent, false)
        return MyViewHolder(itemView)
    }

    private fun initData() {

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemView = holder.itemView
        val video = listVideoDownloadedAdapter?.get(position)

        //test convert long to date string
        val second: Long = video!!.videoDuration % 60
        val minute: Long = video!!.videoDuration / (1 * 60) % 60

        val time = String.format("%02d:%02d", minute, second)
        Log.d("onBindViewHolder", "onBindViewHolder: second minute$minute $second")
        Log.d("onBindViewHolder", "onBindViewHolder:time$time ")

        //end test convert long to date string
        itemView.tvTimeLengthOfVideo.text = time

        var videoPosition = 0

        val uri: Uri = Uri.fromFile(video.file)
        Glide.with(context).load(uri).thumbnail(0.1f).into(itemView.ivVideoImage)


        itemView.ivVideoImage.setOnClickListener {
            Constants.videoLinkUrl = video.url.toString()
            ///

//            listener.OnItemThemeClick(video.file.toString())
            listener.OnItemThemeClick(uri.toString())
        }
//        dialogDeleteFile = onCreateNormalDialog(context, R.layout.dialog_delete_video, true)
//        dialogMore = Utils.onCreateAnimDialogBottom(
//            context,
//            R.layout.dialog_more,
//            R.style.DialogSlideAnim,
//            true
//        )

        itemView.btnShare.setOnClickListener {
            Toast.makeText(context, "share", Toast.LENGTH_SHORT).show()
            val fileUri = FileProvider.getUriForFile(
                context, BuildConfig.APPLICATION_ID + ".provider", File(video.file.toString())
            )
            val intent: Intent? = Utils.createFileShareIntent(
                context.getString(R.string.txt_share), fileUri
            )
            context.startActivity(Intent.createChooser(intent, "share.."))
        }

//        (dialogDeleteFile?.findViewById<View>(R.id.btnDelete1) as TextView).setOnClickListener {
//            Log.d("", "onBindViewHolder11: +position:$videoPosition")
////            listVideoDownloadedAdapter?.removeAt(videoPosition)
//            listVideoDownloadedAdapter?.let {
//                if (it.size > position) {
//                    it.drop(position)
//                }
//            }
//
//            Log.d("", "onBindViewHolder11: +listVideoDownloadedAdapter:$listVideoDownloadedAdapter")
//            notifyDataSetChanged()
//            dialogDeleteFile?.dismiss()
//            Toast.makeText(context, "btnDelete", Toast.LENGTH_SHORT).show()
//        }
//
//        (dialogDeleteFile?.findViewById<View>(R.id.btnCancel) as TextView).setOnClickListener {
//            dialogDeleteFile?.dismiss()
//            Toast.makeText(context, "btnCancel", Toast.LENGTH_SHORT).show()
//        }

        itemView.btnCopyUrl.setOnClickListener {
            Toast.makeText(context, R.string.txt_copied_to_clipbroad, Toast.LENGTH_SHORT).show()
            Utils.copyToClipboard(context, video.url.toString())
        }

        itemView.btnViewOnApp.setOnClickListener {
            Utils.openInApp(video.url.toString(), context, Constants.packageAppName)
        }

//        (dialogMore?.findViewById<View>(R.id.btnCopyUrl) as LinearLayout).setOnClickListener {
//            dialogMore?.dismiss()
//
//        }
//
//        (dialogMore?.findViewById<View>(R.id.btnViewOnApp) as LinearLayout).setOnClickListener {
//            dialogMore?.dismiss()
//        }
//
//        (dialogMore?.findViewById<View>(R.id.btnRepost) as LinearLayout).setOnClickListener {
//            dialogMore?.dismiss()
//            //TODO làm chức năng post video đó lên facebook. Để ngõ đó cho đến khi cần thiết làm chức năng này
//            //https://www.google.com/search?q=how+to+post+a+video+on+facebook+programmatically+in+android&sxsrf=AJOqlzXy8xQfTakXbX7ZH85X2MkSTVCfaA%3A1675068426235&ei=CoTXY66DDpn_z7sPoIqQ4AM&oq=how+to+post+a+video+on+facebook+programma+in+android&gs_lcp=Cgxnd3Mtd2l6LXNlcnAQAxgBMggIIRCgARDDBDIICCEQoAEQwwQyCAghEKABEMMEOgoIABBHENYEELADSgQIQRgASgQIRhgAUP8aWLEcYLYraAFwAXgAgAG-AYgB2AKSAQMwLjKYAQCgAQHIAQjAAQE&sclient=gws-wiz-serp
//        }

    }
    override fun getItemCount(): Int {
        return listVideoDownloadedAdapter!!.size
    }


}