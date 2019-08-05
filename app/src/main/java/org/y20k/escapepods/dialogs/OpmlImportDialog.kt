package org.y20k.escapepods.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import org.y20k.escapepods.R
import org.y20k.escapepods.helpers.LogHelper

class OpmlImportDialog(private var opmlImportDialogListener: OpmlImportDialogListener) {

    /* Interface used to communicate back to activity */
    interface OpmlImportDialogListener {
        fun onOpmlImportDialog(feedUrls: Array<String>) {
        }
    }

    /* Define log tag */
    private val TAG = LogHelper.makeLogTag(OpmlImportDialog::class.java.simpleName)


    /* Construct and show dialog */
    fun show(context: Context, feedUrls: Array<String>) {
        // prepare dialog builder
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        // get views
        val view: View = inflater.inflate(R.layout.dialog_opml_import, null)
        val opmlImportTitleView: TextView = view.findViewById(R.id.dialog_opml_import_title) as TextView
        val opmlImportMessageView: TextView = view.findViewById(R.id.dialog_opml_import_message) as TextView
        val opmlImportDetailsLinkView: TextView = view.findViewById(R.id.dialog_opml_import_details_link) as TextView
        val opmlImportDetailsView: TextView = view.findViewById(R.id.dialog_opml_import_details) as TextView

        // set dialog view
        builder.setView(view)

        // set text views
        opmlImportTitleView.text = context.getString(R.string.dialog_opml_import_title)
        val numberOfFeeds: Int = feedUrls.size

        when (numberOfFeeds) {

            // CASE: No new feeds found
            0 -> {
                // set message
                opmlImportMessageView.text = context.getString(R.string.dialog_opml_import_message_error)

                // change details visibility
                opmlImportDetailsLinkView.setVisibility(View.GONE)
                opmlImportDetailsView.setVisibility(View.GONE)

                // add okay button
                builder.setPositiveButton(R.string.dialog_generic_button_okay) { _, _ ->
                    // listen for click on okay button
                    // do nothing
                }

            }

            // CASE: New feeds found
            else -> {
                // set message
                val opmlImportMessage: String = "${context.getString(R.string.dialog_opml_import_message)} $numberOfFeeds"
                opmlImportMessageView.text = opmlImportMessage

                // build details string
                val detailsText: String
                val detailsBuilder: StringBuilder = StringBuilder()
                feedUrls.forEach {
                    detailsBuilder.append("$it \n")
                }
                detailsText = detailsBuilder.toString()

                // set details text
                opmlImportDetailsView.text = detailsText
                opmlImportDetailsView.movementMethod = ScrollingMovementMethod()

                // change details visibility
                opmlImportDetailsLinkView.setVisibility(View.VISIBLE)
                opmlImportDetailsView.setVisibility(View.GONE)

                // show and hide details on click
                opmlImportDetailsLinkView.setOnClickListener {
                    when (opmlImportDetailsView.visibility) {
                        View.GONE -> opmlImportDetailsView.setVisibility(View.VISIBLE)
                        View.VISIBLE -> opmlImportDetailsView.setVisibility(View.GONE)
                    }
                }

                // add okay ("import") button
                builder.setPositiveButton(R.string.dialog_opml_import_button_okay) { _, _ ->
                    // listen for click on okay button
                    opmlImportDialogListener.onOpmlImportDialog(feedUrls)
                }
                // add cancel button
                builder.setNegativeButton(R.string.dialog_generic_button_cancel) { _, _ ->
                    // listen for click on cancel button
                    // do nothing
                }

            }
        }

        // display dialog
        builder.show()
    }


}