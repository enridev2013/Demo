package com.test.locator;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.ListActivity;

public class MainActivity extends Activity implements LocationListener {

    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
    //public ArrayAdapter<String> adapter;
    public CustomArrayAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
        }

        //Initialize lists
        final ArrayList<RowItem> RowItemList = new ArrayList<RowItem>();
        RowItemList.add(new RowItem("Android", "Ok"));
        RowItemList.add(new RowItem("iPhone", "Ok"));
        RowItemList.add(new RowItem("WindowsMobile", "Buu"));
        RowItemList.add(new RowItem("Blackberry", "Buu"));
        RowItemList.add(new RowItem("WebOS", "Buu"));

        final ArrayList<String> RowItemListSingle = new ArrayList<String>();
        RowItemListSingle.add("Android");
        RowItemListSingle.add("iPhone");
        RowItemListSingle.add("WindowsMobile");
        RowItemListSingle.add("Blackberry");
        RowItemListSingle.add("WebOS");

        final ListView listview = (ListView) findViewById(R.id.listView);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS"};
        String[] values2 = new String[] { "Ok", "Ok", "Buu",
                "Buu", "Buu"};
        /*
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i)
        {
            list.add(values[i]);
        }
        */
        //adapter = new ArrayAdapter<String>(this,
        //        R.layout.row_layout, R.id.label, values);
        //adapter = new CustomArrayAdapter(this,
        //                R.layout.row_layout, R.id.label, values, values2);
        //adapter = new CustomArrayAdapter(this,
         //       R.layout.row_layout, R.id.label, RowItemList, values);
        adapter = new CustomArrayAdapter(this,
                      R.layout.row_layout, R.id.label, RowItemListSingle, RowItemList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id)
            {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(1000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                adapter.remove(item, position);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }
        });

    }

    private class CustomArrayAdapter extends ArrayAdapter<String>
    {
        private Context m_context;
        private int m_iRowResourceID;
        private String[] m_asValues;
        private String[] m_asValues2;
        private LayoutInflater mInflater;
        private ArrayList<String> m_SingleList;
        private ArrayList<RowItem> objListRowItem;

        public CustomArrayAdapter(Context context, int textViewResourceId, int iRowResourceId,
                                  ArrayList<String> objRowItems, ArrayList<RowItem> objRowItemsList)
        {
            super(context, textViewResourceId, R.layout.row_layout2, objRowItems);
            this.m_context = context;
            this.m_iRowResourceID = iRowResourceId;
            this.m_SingleList = objRowItems;
            this.objListRowItem = objRowItemsList;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public CustomArrayAdapter(Context context, int textViewResourceId, int iRowResourceId,
                                  ArrayList<RowItem> objRowItems, String[] objects)
        {
            super(context, textViewResourceId, R.layout.row_layout2, objects);
            this.m_context = context;
            this.m_iRowResourceID = iRowResourceId;
            this.objListRowItem = objRowItems;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public CustomArrayAdapter(Context context, int textViewResourceId, int iRowResourceId,
                                  String[] objects, String[] objects2)

        {
            super(context, textViewResourceId, R.layout.row_layout2, objects);
            this.m_context = context;
            this.m_iRowResourceID = iRowResourceId;
            this.m_asValues = objects;
            this.m_asValues2 = objects2;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = mInflater.inflate(R.layout.row_layout2, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.label1);
            TextView textView2 = (TextView) rowView.findViewById(R.id.label2);

            String sLable1 = this.m_SingleList.get(position);
            textView.setText(sLable1);
            String sLable2 = this.objListRowItem.get(position).m_sLable2;
            textView2.setText(sLable2);
            return rowView;
        }

        public void add(String sLable1, String sLable2)
        {
            super.add(sLable1);
            this.objListRowItem.add(new RowItem(sLable1, sLable2));
        }

        public void remove(String item, int iPosition)
        {
            super.remove(item);
            this.objListRowItem.remove(iPosition);
        }
    }

    private class RowItem
    {
        public String m_sLable1;
        public String m_sLable2;
        public RowItem(String sLable1, String sLable2)
        {
            this.m_sLable1 = sLable1;
            this.m_sLable2 = sLable2;
        }
    }
    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public void onButtonClick(final View view)
    {
        try
        {
            adapter.add("Enrico", "Ok");
        }
        catch(Exception e)
        {
            String error = e.getMessage();
        }
    }
}

/*
    private class StableArrayAdapter extends ArrayAdapter<String>
    {
        private Context m_context;
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects)
        {
            super(context, textViewResourceId, objects);
            this.m_context = context;
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
    */
