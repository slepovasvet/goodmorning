package ru.slepova.goodmorning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import ru.slepova.goodmorning.res.Categories;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhraseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhraseFragment extends Fragment {
    private static final String ARG_PARAM1 = "category_id";
    private static final String ARG_PARAM2 = "image_r";
    private static final String ARG_PARAM3 = "phrase";

    private Integer category_id;
    private Integer im;
    private String phrase;

    public PhraseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category_id category id.
     * @return A new instance of fragment PhraseFragment.
     */
    public static PhraseFragment newInstance(
            Integer category_id, Integer im, String phrase) {
        PhraseFragment fragment = new PhraseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, category_id);
        args.putInt(ARG_PARAM2, im);
        args.putString(ARG_PARAM3, phrase);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_id = getArguments().getInt(ARG_PARAM1);
            im = getArguments().getInt(ARG_PARAM2);
            phrase = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_phrase, container, false);

        TextView phraseTextView = v.findViewById(R.id.phraseTextView);
        phraseTextView.setText(phrase);

        CardView cv = v.findViewById(R.id.card_view);
        AppCompatTextView catv = v.findViewById(R.id.cat_title);

        switch (category_id) {
            case 1:
                cv.setCardBackgroundColor(getResources().getColor(R.color.phrase_color_1));
                catv.setText(new Categories().Names.get(1).toUpperCase() + "\nФраза дня");
                break;
            case 2:
                 cv.setCardBackgroundColor(getResources().getColor(R.color.phrase_color_2));
                catv.setText(new Categories().Names.get(2).toUpperCase() + "\nФраза дня");
                break;
            case 3:
                cv.setCardBackgroundColor(getResources().getColor(R.color.phrase_color_3));
                catv.setText(new Categories().Names.get(3).toUpperCase() + "\nФраза дня");
                break;
        }

        return v;
    }
}
