package org.dobots.swarmcontrol.robots;

import org.dobots.robots.RobotDevice;
import org.dobots.robots.RobotDevice.OnFinishListener;
import org.dobots.robots.nxt.NXT;
import org.dobots.swarmcontrol.R;
import org.dobots.swarmcontrol.RobotInventory;
import org.dobots.swarmcontrol.SwarmControlActivity;
import org.dobots.utility.CalibrationDialogSelf;
import org.dobots.utility.CalibrationDialogSelf.OnRunClick;
import org.dobots.utility.CalibrationDialogUser;
import org.dobots.utility.DeviceListActivity;
import org.dobots.utility.FeedbackDialog;
import org.dobots.utility.OnButtonPress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RobotCalibration extends Activity {
	
	private RobotDevice m_oRobot;
	
	private Button m_btnCalibrateCircleSelf;
	private Button m_btnCalibrateCircleUser;
	private Button m_btnCalibrateDistance;
	private Button m_btnCalibrateSave;
	private Button m_btnCalibrateDiscard;
	
	private Activity m_oActivity;
	
	private int m_nSpeed;

	public static final String CALIBRATED_SPEED = "CALIBRATED_SPEED";
	
	public static final int ROBOT_CALIBRATION_RESULT = 1060;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		m_oActivity = this;
		
    	int nIndex = (Integer) getIntent().getExtras().get("InventoryIndex");

    	m_oRobot = RobotInventory.getInstance().getRobot(nIndex);

    	setProperties();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case CalibrationDialogSelf.CALIBRATION_RESULT:
			if (resultCode == RESULT_OK) {
				m_nSpeed = data.getExtras().getInt(CalibrationDialogSelf.CALIBRATED_VALUE);
			}
		}
		
	}
	
	private void setProperties() {
		setContentView(R.layout.robot_calibration);
		
		m_btnCalibrateCircleSelf = (Button) findViewById(R.id.btnCalibrate_Circle_Self);
		m_btnCalibrateCircleSelf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CalibrationDialogSelf.createAndShow(m_oActivity, "Calibrate Circle", "Check if Circle was done correctly and adjust if necessary",
						new OnRunClick() {
							
							@Override
							public void onClick(double i_nTime, double i_nSpeed) {
								m_oRobot.executeCircle(i_nTime, i_nSpeed);
							}
						});
			}
		});
		
		m_btnCalibrateCircleUser = (Button) findViewById(R.id.btnCalibrate_Circle_User);
		m_btnCalibrateCircleUser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CalibrationDialogUser.createAndShow(m_oActivity, "Calibrate Circle", "Check if Circle was done correctly and adjust if necessary",
						new OnRunClick() {
							
							@Override
							public void onClick(double i_nTime, double i_nSpeed) {
								m_oRobot.executeCircle(i_nTime, i_nSpeed);
							}
						});
			}
		});
		m_btnCalibrateSave = (Button) findViewById(R.id.btnCalibrate_Save);
		m_btnCalibrateSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent result = new Intent();
				Bundle data = new Bundle();
				data.putInt(CALIBRATED_SPEED, m_nSpeed);
				result.putExtras(data);
				setResult(RESULT_OK, result);
				finish();
			}
		});
		
		m_btnCalibrateDiscard = (Button) findViewById(R.id.btnCalibrate_Discard);
		m_btnCalibrateDiscard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
	}

	public static void createAndShow(Activity i_oActivity, RobotType i_eType, int i_nIndex) {
		Intent intent = new Intent(i_oActivity, RobotCalibration.class);
		intent.putExtra("RobotType", i_eType);
		intent.putExtra("InventoryIndex", i_nIndex);
		i_oActivity.startActivityForResult(intent, ROBOT_CALIBRATION_RESULT);
	}
	
}