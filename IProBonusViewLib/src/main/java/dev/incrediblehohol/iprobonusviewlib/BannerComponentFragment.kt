package dev.incrediblehohol.iprobonusviewlib

import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.incrediblehohol.iprobonusviewlib.databinding.BannerFragmentBinding


private const val CLIENT_ID = "client_id"
private const val DEVICE_ID = "device_id"
private const val TOTAL_TEXT_SIZE = "total_text_size"
private const val BURNING_TEXT_SIZE = "burning_text_size"
private const val ARROW_COLOR = "arrow_color"
private const val TOTAL_TEXT_COLOR = "total_text_color"
private const val BURNING_TEXT_COLOR = "burning_text_color"
private const val DARK_GRADIENT_COLOR = "dark_gradient_color"
private const val LIGHT_GRADIENT_COLOR = "light_gradient_color"

private const val DEF_FLOAT: Float = 0F
private const val DEF_INT: Int = 0

private const val DEF_POINT: Float = 0F
private const val DEF_COORDINATE: Float = 0F

class BannerComponentFragment : Fragment() {

    private val clientId: String by lazy {
        arguments?.getString(CLIENT_ID).orEmpty()
    }

    private val deviceId: String by lazy {
        arguments?.getString(DEVICE_ID).orEmpty()
    }

    private val totalTextSize: Float? by lazy {
        arguments?.getFloat(TOTAL_TEXT_SIZE)?.takeIf { it > DEF_FLOAT }
    }

    private val burningTextSize: Float? by lazy {
        arguments?.getFloat(BURNING_TEXT_SIZE)?.takeIf { it > DEF_FLOAT }
    }

    private val arrowColor: Int? by lazy {
        arguments?.getInt(ARROW_COLOR).takeIf { it != DEF_INT }
    }

    private val totalTextColor: Int? by lazy {
        arguments?.getInt(TOTAL_TEXT_COLOR).takeIf { it != DEF_INT }
    }

    private val burningTextColor: Int? by lazy {
        arguments?.getInt(BURNING_TEXT_COLOR).takeIf { it != DEF_INT }
    }

    private val darkGradientColor: Int? by lazy {
        arguments?.getInt(DARK_GRADIENT_COLOR).takeIf { it != DEF_INT }
    }

    private val lightGradientColor: Int? by lazy {
        arguments?.getInt(LIGHT_GRADIENT_COLOR).takeIf { it != DEF_INT }
    }


    private lateinit var binding: BannerFragmentBinding
    private val vm: BannerViewModel by viewModels { BannerViewModelFactory(clientId, deviceId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BannerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.vm = vm
        binding.lifecycleOwner = viewLifecycleOwner

        applySettings()

        binding.buttonArrow.setOnClickListener {
            requireContext().let {
                Toast.makeText(it, "Do something", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonInfo.setOnClickListener {
            requireContext().let {
                Toast.makeText(it, "Do somthing else", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyColors() {
        val darkColor =
            darkGradientColor ?: resources.getColor(R.color.dark_gradient_color, null)
        val lightColor =
            lightGradientColor ?: resources.getColor(R.color.light_gradient_color, null)

        applyToBackgroundView(darkColor, lightColor)
        applyToInfoButton(darkColor, lightColor)
    }

    private fun applyToBackgroundView(darkColor: Int, lightColor: Int) {
        val colorList = intArrayOf(darkColor, lightColor)

        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorList)

        binding.viewBackground.background = gradientDrawable
    }

    private fun applyToInfoButton(darkColor: Int, lightColor: Int) {
        val src = BitmapFactory.decodeResource(resources, R.drawable.info)

        val w = src.width
        val h = src.height

        val result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(result)

        canvas.drawBitmap(src, DEF_POINT, DEF_POINT, null)

        val paint = Paint()
        val shader =
            LinearGradient(
                DEF_COORDINATE,
                DEF_COORDINATE,
                DEF_COORDINATE,
                h.toFloat(),
                darkColor,
                lightColor,
                Shader.TileMode.CLAMP
            )
        paint.shader = shader
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawRect(DEF_POINT, DEF_POINT, w.toFloat(), h.toFloat(), paint)


        binding.buttonInfo.setImageBitmap(result)
    }

    private fun applySettings() {
        totalTextSize?.let {
            binding.textViewTotalBonuses.textSize = it.dpToPx
        }

        burningTextSize?.let {
            binding.textViewBurningBonuses.textSize = it.dpToPx
            binding.textViewBurningDate.textSize = it.dpToPx
        }

        arrowColor?.let {
            binding.buttonArrow.drawable.setTint(it)
        }

        totalTextColor?.let {
            binding.textViewTotalBonuses.setTextColor(it)
        }

        burningTextColor?.let {
            binding.textViewBurningDate.setTextColor(it)
            binding.textViewBurningBonuses.setTextColor(it)
        }

        darkGradientColor?.let {
            applyColors()
        }

        lightGradientColor?.let {
            applyColors()
        }
    }

    companion object {

        /**
         * U must pass @param clientId and @param deviceId
         * textSizes must be float like sp
         * colors by Int
         */
        fun newInstance(
            clientId: String,
            deviceId: String,
            totalTextSize: Float? = null,
            burningTextSize: Float? = null,
            arrowColor: Int? = null,
            totalTextColor: Int? = null,
            burningTextColor: Int? = null,
            darkGradientColor: Int? = null,
            lightGradientColor: Int? = null
        ): BannerComponentFragment {
            return BannerComponentFragment().apply {
                arguments = bundleOf(
                    CLIENT_ID to clientId,
                    DEVICE_ID to deviceId,
                    TOTAL_TEXT_SIZE to totalTextSize,
                    BURNING_TEXT_SIZE to burningTextSize,
                    ARROW_COLOR to arrowColor,
                    TOTAL_TEXT_COLOR to totalTextColor,
                    BURNING_TEXT_COLOR to burningTextColor,
                    DARK_GRADIENT_COLOR to darkGradientColor,
                    LIGHT_GRADIENT_COLOR to lightGradientColor
                )
            }
        }
    }
}
