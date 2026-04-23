//MainActivity
package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val cardinfo=listOf(
        Cardinfo(
            "",
            1234.566,
            "jfjkfj"
        ),
        Cardinfo(
            "" ,
            1223.323,
            "fjdsf"
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val fragmentOne= CardProductFragment.newInstance(cardinfo[0])
        val fragmentTwo= CardProductFragment.newInstance(cardinfo[1])

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,fragmentOne)
            .commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView2,fragmentTwo)
            .commit()


    }
}



// CardProductFragment
package com.example.myapplication

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentCardProductBinding

class CardProductFragment : Fragment() {
    private var _binding: FragmentCardProductBinding? = null

    private val binding get() = _binding!!
    companion object {
        const val KEY_IMAGE="kgkgk"
        const val KEY_PRICE="kgkkk"
        const val KEY_ABOUT="kglgk"

        fun newInstance(data: Cardinfo) : CardProductFragment{
            return CardProductFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_IMAGE,data.imgaeUrl)
                    putDouble(KEY_PRICE,data.price)
                    putString(KEY_ABOUT,data.about)
                }

            }

        }
    }

    private val viewModel: CardProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val data=arguments?.getString(KEY_PRICE) косяк с типом
        val card = Cardinfo(
            arguments?.getString(KEY_IMAGE, "none")?: "kl",
            arguments?.getDouble(KEY_PRICE, 1.0 )?: 1.0,
            arguments?.getString(KEY_ABOUT, "aaaaa")?:"none"
        )
        _binding = FragmentCardProductBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.priceTextView.text=card.price.toString()
        binding.priceTextView.text=card.about




        return view

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//Cardinfo
package com.example.myapplication

data class Cardinfo(

    val imgaeUrl: String,
    val price: Double,
    val about: String


)
