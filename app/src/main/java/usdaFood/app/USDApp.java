package usdaFood.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import usdaFood.usda.USDAClient;
import usdaFood.usda.USDAClientBuilder;


public class USDApp extends AppCompatActivity {
    private USDAClient usdaClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public USDAClient getUSDAClient() {
        usdaClient = new USDAClientBuilder()
                .addTokenAPI("bjkUHzRmbXY63rgb8WOCEghtRg7PQ59odUCieLkR").build();
              //  .addNetworkOperations(new MozillaNetworkClient()).build();
        return usdaClient;
    }

    //	System.out.println( usdaClient.searchFoodReport("Milk"));
    //usdaClient.searchFood("Apple");
}
