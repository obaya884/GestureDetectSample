package app.takumi.obayashi.gesturedetectsample

import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.Prediction
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.save_dialog.view.*

class MainActivity : AppCompatActivity() {

    var gestureLibrary: GestureLibrary? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // GestureLibrary取得
        gestureLibrary = getMyGestureLibrary()
        // ジェスチャー登録設定
        setAddGesture();
        // ジェスチャー読取設定
        setReadGesture();
    }

    // GestureLibrary取得
    private fun getMyGestureLibrary(): GestureLibrary? {
        return GestureLibraries.fromPrivateFile(this@MainActivity, "xxxx.gusture")
    }

    // ジェスチャー登録設定
    private fun setAddGesture() {
        // ジェスチャー色
        addGesture.gestureColor = Color.RED
        // ジェスチャー幅
        addGesture.gestureStrokeWidth = 5F

        // ジェスチャーリスナー
        addGesture.addOnGesturePerformedListener { _, gesture ->
            val saveDialog = layoutInflater.inflate(R.layout.save_dialog, null)

            // ジェスチャーイメージ
            val bitmap = gesture.toBitmap(128, 128, 10, -0x10000)
            saveDialog.show.setImageBitmap(bitmap)

            // ダイアログ表示
            AlertDialog.Builder(this@MainActivity)
                .setView(saveDialog)
                .setPositiveButton("保存") { _, _ ->
                    // ジェスチャー保存
                    gestureLibrary!!.addGesture(saveDialog.gesture_name.text.toString(), gesture)
                    // 保存
                    gestureLibrary!!.save()
                    Toast.makeText(this@MainActivity, "登録完了", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("取消", null)
                .show()

        }
    }

    private fun setReadGesture() {
        // ジェスチャー色
        readGesture.gestureColor = Color.BLUE;
        // ジェスチャー幅
        readGesture.gestureStrokeWidth = 5F;

        // ジェスチャーリスナー
        readGesture.addOnGesturePerformedListener { _, gesture ->
            val predictions: List<Prediction> = gestureLibrary!!.recognize(gesture)
            var cnt = 0
            for (p in predictions) {
                if (p.score > 2.0) {
                    Toast.makeText(
                        this@MainActivity,
                        "ジェスチャー名 :" + p.name +
                                "\nジェスチャー類似度 :" + p.score,
                        Toast.LENGTH_SHORT
                    ).show()
                    cnt++
                }
            }
            if (cnt == 0) {
                Toast.makeText(
                    this@MainActivity,
                    "検出できませんでした",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
