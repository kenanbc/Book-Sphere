package com.example.booksbazar.ui.addNew;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.booksbazar.R;

import org.w3c.dom.Text;

public class AddNewFragment extends Fragment {

    private AddNewViewModel mViewModel;
    private TextView newTitleInput;
    private TextView newAuthorInput;
    private TextView newGenreInput;
    private EditText newPriceInput;
    private TextView newImageInput;


    public static AddNewFragment newInstance() {
        return new AddNewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_new, container, false);

        newTitleInput = root.findViewById(R.id.newTitleInput);
        newAuthorInput = root.findViewById(R.id.newAuthorInput);
        newGenreInput = root.findViewById(R.id.newGenreInput);
        newPriceInput = root.findViewById(R.id.newPriceInput);
        newImageInput = root.findViewById(R.id.newImageInput);

        return root;
    }


}