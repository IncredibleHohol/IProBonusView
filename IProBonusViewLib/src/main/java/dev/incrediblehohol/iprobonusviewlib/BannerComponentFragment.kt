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


const val CLIENT_ID = "client_id"
const val DEVICE_ID = "device_id"
const val TOTAL_TEXT_SIZE = "total_text_size"
const val BURNING_TEXT_SIZE = "burning_text_size"
const val ARROW_COLOR = "arrow_color"
const val TOTAL_TEXT_COLOR = "total_text_color"
const val BURNING_TEXT_COLOR = "burning_text_color"
const val DARK_GRADIENT_COLOR = "dark_gradient_color"
const val LIGHT_GRADIENT_COLOR = "light_gradient_color"

class BannerComponentFragment : Fragment() {

    private val clientId: String by lazy {
        arguments?.getString(CLIENT_ID).orEmpty()
    }

    private val deviceId: String by lazy {
        arguments?.getString(DEVICE_ID).orEmpty()
    }

    private val totalTextSize: Float? by lazy {
        arguments?.getFloat(TOTAL_TEXT_SIZE)?.takeIf { it > 0F }
    }

    private val burningTextSize: Float? by lazy {
        arguments?.getFloat(BURNING_TEXT_SIZE)?.takeIf { it > 0F }
    }

    private val arrowColor: Int? by lazy {
        arguments?.getInt(ARROW_COLOR).takeIf { it != 0 }
    }

    private val totalTextColor: Int? by lazy {
        arguments?.getInt(TOTAL_TEXT_COLOR).takeIf { it != 0 }
    }

    private val burningTextColor: Int? by lazy {
        arguments?.getInt(BURNING_TEXT_COLOR).takeIf { it != 0 }
    }

    private val darkGradientColor: Int? by lazy {
        arguments?.getInt(DARK_GRADIENT_COLOR).takeIf { it != 0 }
    }

    private val lightGradientColor: Int? by lazy {
        arguments?.getInt(LIGHT_GRADIENT_COLOR).takeIf { it != 0 }
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
            darkGradientColor ?: resources.getColor(R.color.first_gradient_color, null)
        val lightColor =
            lightGradientColor ?: resources.getColor(R.color.second_gradient_color, null)

        applyToBackgroundView(darkColor, lightColor)
        applyToInfoButton(darkColor, lightColor)
    }

    private fun applyToBackgroundView(darkColor: Int, lightColor: Int) {
        val colorList = IntArray(2)
        colorList[0] = darkColor
        colorList[1] = lightColor

        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorList)

        binding.viewBackground.background = gradientDrawable
    }

    private fun applyToInfoButton(darkColor: Int, lightColor: Int) {
        val src = BitmapFactory.decodeResource(resources, R.drawable.info)

        val w = src.width
        val h = src.height

        val result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(result)

        canvas.drawBitmap(src, 0F, 0F, null)

        val paint = Paint()
        val shader =
            LinearGradient(0F, 0F, 0F, h.toFloat(), darkColor, lightColor, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawRect(0F, 0F, w.toFloat(), h.toFloat(), paint)


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
