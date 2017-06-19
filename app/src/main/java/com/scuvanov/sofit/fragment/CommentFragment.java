package com.scuvanov.sofit.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scuvanov.sofit.R;
import com.scuvanov.sofit.adapter.CommentAdapter;
import com.scuvanov.sofit.model.Comment;
import com.scuvanov.sofit.model.Workout;
import com.scuvanov.sofit.utils.DateUtil;
import com.scuvanov.sofit.utils.FirebaseDatabaseUtil;

public class CommentFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String BUNDLE_WORKOUT = "WORKOUT";

    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Button btnAdd, btnBack;
    private View view;
    private MaterialDialog mCommentDialog;
    private EditText etComment;


    private CommentAdapter commentAdapter;
    private Workout currentWorkout;
    private RecyclerView mCommentsRecycler;

    private static FirebaseUser mUser;

    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CommentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_comment, container, false);
        init();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void init() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        btnAdd = (Button) view.findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(this);

        btnBack = (Button) view.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(this);

        currentWorkout = (Workout) getArguments().getSerializable(BUNDLE_WORKOUT);

        commentAdapter = new CommentAdapter(getActivity(), currentWorkout);
        mCommentsRecycler = (RecyclerView) view.findViewById(R.id.rvComment);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentsRecycler.setAdapter(commentAdapter);
        mCommentsRecycler.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        mCommentsRecycler.setFadingEdgeLength(0);
        mCommentsRecycler.setFitsSystemWindows(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                getActivity().getFragmentManager().popBackStack();
                break;
            case R.id.buttonAdd:

                boolean wrapInScrollView = false;
                mCommentDialog = new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.dialog_comment, wrapInScrollView)
                        .title("Add Comment")
                        .titleColorRes(R.color.primary_blue)
                        .positiveText("ACCEPT")
                        .negativeText("CANCEL")
                        .negativeColorRes(R.color.material_grey_900)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);

                                String commentStr = etComment.getText().toString();
                                Comment mComment = new Comment();
                                mComment.setUid(mUser.getUid());
                                mComment.setComment(commentStr);
                                mComment.setCreated_date(DateUtil.getCurrentDate());

                                FirebaseDatabaseUtil.createComment(mComment, currentWorkout.getId());
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        }).build();


                View mCommentDialogView = mCommentDialog.getCustomView();
                etComment = (EditText) mCommentDialogView.findViewById(R.id.etComment);
                mCommentDialog.show();
                break;
        }

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
