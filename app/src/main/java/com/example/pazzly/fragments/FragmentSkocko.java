package com.example.pazzly.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pazzly.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FragmentSkocko extends Fragment {
    private View view;
    private ImageView aa1,aa2,aa3,aa4;
    private ImageView ba1,ba2,ba3,ba4;
    private ImageView ca1,ca2,ca3,ca4;
    private ImageView da1,da2,da3,da4;
    private ImageView ea1,ea2,ea3,ea4;
    private ImageView fa1,fa2,fa3,fa4;
    private ImageView sym1,sym2,sym3,sym4,sym5,sym6;

    private int red=0;
    private int yellow=0;

    private TextView ab1,ab2,ab3,ab4;

    private TextView bb1,bb2,bb3,bb4;

    private TextView cb1,cb2,cb3,cb4;

    private TextView db1,db2,db3,db4;

    private TextView eb1,eb2,eb3,eb4;

    private TextView fb1,fb2,fb3,fb4;

    private int rowCounter=0;
    private int elementCounter=0;

    List<ImageView> row1List = new ArrayList<>();
    List<ImageView> row2List = new ArrayList<>();
    List<ImageView> row3List = new ArrayList<>();
    List<ImageView> row4List = new ArrayList<>();
    List<ImageView> row5List = new ArrayList<>();
    List<ImageView> row6List = new ArrayList<>();

    List<TextView> rezAList = new ArrayList<>();
    List<TextView> rezBList = new ArrayList<>();
    List<TextView> rezCList = new ArrayList<>();
    List<TextView> rezDList = new ArrayList<>();
    List<TextView> rezEList = new ArrayList<>();
    List<TextView> rezFList = new ArrayList<>();
    List<List<TextView>>allRezList=new ArrayList<>();


    List<List<ImageView>> allRowsList = new ArrayList<>();
    List<ImageView>allSymbolsList=new ArrayList<>();

    List<Integer>generatedAnswer=new ArrayList<>();
    List<Integer>submittedAnswer= new ArrayList<>();

    private FragmentSkocko.SubmitCallbackSkocko callbackSkocko;
    public interface SubmitCallbackSkocko {
        void onSubmissionSkocko(int points);
    }

    public void setSubmitCallbackSkocko(FragmentSkocko.SubmitCallbackSkocko callback) {
        callbackSkocko = callback;
    }

    public static FragmentSkocko newInstance() {
        return new FragmentSkocko();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_skocko, container, false);

        initializePictures();
        initializeSymbols();
        initializeListeners();
        initializeRezults();
        generateRandomAnswer();







        return view;
    }
    public void initializePictures(){

        aa1=view.findViewById(R.id.aa1);
        aa2=view.findViewById(R.id.aa2);
        aa3=view.findViewById(R.id.aa3);
        aa4=view.findViewById(R.id.aa4);

        ba1=view.findViewById(R.id.ba1);
        ba2=view.findViewById(R.id.ba2);
        ba3=view.findViewById(R.id.ba3);
        ba4=view.findViewById(R.id.ba4);

        ca1=view.findViewById(R.id.ca1);
        ca2=view.findViewById(R.id.ca2);
        ca3=view.findViewById(R.id.ca3);
        ca4=view.findViewById(R.id.ca4);

        da1=view.findViewById(R.id.da1);
        da2=view.findViewById(R.id.da2);
        da3=view.findViewById(R.id.da3);
        da4=view.findViewById(R.id.da4);

        ea1=view.findViewById(R.id.ea1);
        ea2=view.findViewById(R.id.ea2);
        ea3=view.findViewById(R.id.ea3);
        ea4=view.findViewById(R.id.ea4);

        fa1=view.findViewById(R.id.fa1);
        fa2=view.findViewById(R.id.fa2);
        fa3=view.findViewById(R.id.fa3);
        fa4=view.findViewById(R.id.fa4);





        row1List= Arrays.asList(aa1,aa2,aa3,aa4);
        row2List= Arrays.asList(ba1,ba2,ba3,ba4);
        row3List=Arrays.asList(ca1,ca2,ca3,ca4);
        row4List=Arrays.asList(da1,da2,da3,da4);
        row5List=Arrays.asList(ea1,ea2,ea3,ea4);
        row6List=Arrays.asList(fa1,fa2,fa3,fa4);
        allRowsList=Arrays.asList(row1List,row2List,row3List,row4List,row5List,row6List);












    }
    public void initializeRezults(){

        ab1=view.findViewById(R.id.ab1);
        ab2=view.findViewById(R.id.ab2);
        ab3=view.findViewById(R.id.ab3);
        ab4=view.findViewById(R.id.ab4);

        bb1=view.findViewById(R.id.bb1);
        bb2=view.findViewById(R.id.bb2);
        bb3=view.findViewById(R.id.bb3);
        bb4=view.findViewById(R.id.bb4);

        cb1=view.findViewById(R.id.cb1);
        cb2=view.findViewById(R.id.cb2);
        cb3=view.findViewById(R.id.cb3);
        cb4=view.findViewById(R.id.cb4);

        db1=view.findViewById(R.id.db1);
        db2=view.findViewById(R.id.db2);
        db3=view.findViewById(R.id.db3);
        db4=view.findViewById(R.id.db4);

        eb1=view.findViewById(R.id.eb1);
        eb2=view.findViewById(R.id.eb2);
        eb3=view.findViewById(R.id.eb3);
        eb4=view.findViewById(R.id.eb4);

        fb1=view.findViewById(R.id.fb1);
        fb2=view.findViewById(R.id.fb2);
        fb3=view.findViewById(R.id.fb3);
        fb4=view.findViewById(R.id.fb4);

        rezAList= Arrays.asList(ab1,ab2,ab3,ab4);
        rezBList= Arrays.asList(bb1,bb2,bb3,bb4);
        rezCList=Arrays.asList(cb1,cb2,cb3,cb4);
        rezDList=Arrays.asList(db1,db2,db3,db4);
        rezEList=Arrays.asList(eb1,eb2,eb3,eb4);
        rezFList=Arrays.asList(fb1,fb2,fb3,fb4);
        allRezList= new ArrayList<>(Arrays.asList(rezAList,rezBList,rezCList,rezDList,rezEList,rezFList));






    }

    public void initializeSymbols(){



        sym1=view.findViewById(R.id.sym1);



        sym2=view.findViewById(R.id.sym2);
        sym3=view.findViewById(R.id.sym3);
        sym4=view.findViewById(R.id.sym4);
        sym5=view.findViewById(R.id.sym5);
        sym6=view.findViewById(R.id.sym6);

        allSymbolsList=Arrays.asList(sym1,sym2,sym3,sym4,sym5,sym6);


    }
    public void initializeListeners(){
        int symbolIndex=0;
        for(ImageView symbol:allSymbolsList){
            int finalSymbolIndex = symbolIndex;
            symbol.setOnClickListener(view1 -> {
                allRowsList.get(rowCounter).get(elementCounter).setImageDrawable(symbol.getDrawable());
                elementCounter++;
                submittedAnswer.add(finalSymbolIndex);
                Log.d("FINALSYMBOL", String.valueOf(finalSymbolIndex));
                if(elementCounter==4){

                    elementCounter=0;
                    rowCounter++;
                    checkRow();
                    showResults(rowCounter-1);
                    submittedAnswer=new ArrayList<>();
                }
                if (rowCounter==6){
                    rowCounter=0;
                }

            });
            symbolIndex++;
        }

    }

    public void checkRow(){
        List<Integer>copyGenerated=new ArrayList<>(generatedAnswer);

        for(int i=0;i<4;i++){
            if (submittedAnswer.get(i)==copyGenerated.get(i)){
                red++;
                Log.d("KOPIGENERATED", copyGenerated.get(i).toString());
                Log.d("SABMITED", submittedAnswer.get(i).toString());
                copyGenerated.set(i,null);
                submittedAnswer.set(i,null);


            }

        }

        for(int i=0;i<4;i++){
            if (submittedAnswer.get(i)!=null &&copyGenerated.contains(submittedAnswer.get(i))){
                yellow++;
                copyGenerated.set(i,null);
                submittedAnswer.set(i,null);

            }

        }

//


    }

    public void showResults(int resultRow){
        for (int i=0;i<red;i++){
            allRezList.get(resultRow).get(i).setBackgroundColor(Color.RED);

        }
        for(int i=red;i<yellow+1;i++){
            allRezList.get(resultRow).get(i).setBackgroundColor(Color.YELLOW);
        }
        calculatePoints();

        red=0;
        yellow=0;

    }
    public void generateRandomAnswer(){

//        for(int i=0;i<4;i++){
//            Random random = new Random();
//            generatedAnswer.add(random.nextInt(6));
//            Log.d("SKOCKORESENJE", generatedAnswer.toString());
//
//
//        }
        generatedAnswer.add(1);
        generatedAnswer.add(2);
        generatedAnswer.add(3);
        generatedAnswer.add(4);
    }

    public void calculatePoints(){
        int points = 0;
        if (red == 4) {
            switch (rowCounter) {
                case 1:
                case 2:
                    points = 20;
                    break;
                case 3:
                case 4:
                    points = 15;
                    break;
                case 5:
                case 6:
                    points = 10;
                    break;
            }
        }
        callbackSkocko.onSubmissionSkocko(points);
    }

}
