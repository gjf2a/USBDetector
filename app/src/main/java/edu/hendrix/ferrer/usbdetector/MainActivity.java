package edu.hendrix.ferrer.usbdetector;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private UsbManager mUsbManager;
    private TextView textView;
    private UsbDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        textView.setText("Awaiting instructions");
    }

    public void deviceChecker(View view) {
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        if (mUsbManager == null) {
            textView.setText("No USB manager");
        } else {
            String output = "Devices:";
            for (Map.Entry<String,UsbDevice> entry: mUsbManager.getDeviceList().entrySet()) {
                device = entry.getValue();
                output += " Name: " + device.getDeviceName()
                        + "\nVersion: " + device.getVersion()
                        + "\nManufacturer: " + device.getManufacturerName()
                        + "\nProduct: " + device.getProductName()
                        + "\nVendorID: " + device.getVendorId()
                        + "\nProductID: " + device.getProductId()
                        + "\nClass: " + device.getDeviceClass()
                        + "\nSubclass: " + device.getDeviceSubclass()
                        + "\nProtocol: " + device.getDeviceProtocol()
                        + "\ninterface count: " + device.getInterfaceCount();
                for (int i = 0; i < device.getInterfaceCount(); i++) {
                    UsbInterface inter = device.getInterface(i);
                    output += "\nInterface " + i + ": "
                            + "\nName: " + inter.getName()
                            + "\nEndpoints: " + inter.getEndpointCount();
                    for (int j = 0; j < inter.getEndpointCount(); j++) {
                        UsbEndpoint endpoint = inter.getEndpoint(j);
                        output += "\nNumber: " + endpoint.getEndpointNumber()
                                + "\nDirection: " + decodeEndpointDirection(endpoint)
                                + "\nType: " + decodeEndpointType(endpoint)
                                + "\nMax Packet Size: " + endpoint.getMaxPacketSize();
                    }
                }
            }
            output += ".";
            textView.setText(output);
        }
    }

    public String decodeEndpointType(UsbEndpoint endpoint) {
        switch (endpoint.getType()) {
            case UsbConstants.USB_ENDPOINT_XFER_CONTROL :
                return "Endpoint Zero";
            case UsbConstants.USB_ENDPOINT_XFER_ISOC :
                return "Isochronous Endpoint";
            case UsbConstants.USB_ENDPOINT_XFER_BULK :
                return "Bulk Endpoint";
            case UsbConstants.USB_ENDPOINT_XFER_INT :
                return "Interrupt Endpoint";
            default:
                return "Unknown Endpoint Type";
        }
    }

    public String decodeEndpointDirection(UsbEndpoint endpoint) {
        switch (endpoint.getDirection()) {
            case UsbConstants.USB_DIR_IN:
                return "Device to Host";
            case UsbConstants.USB_DIR_OUT:
                return "Host to Device";
            default:
                return "Unknown direction";
        }
    }
}
