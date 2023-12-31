package com.example.onagoogle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;

import org.checkerframework.checker.nullness.qual.NonNull;

public class HomeActivity extends AppCompatActivity {

    TextView name, mail;
    ImageButton imgButtonLookupDictionary,
            imgButtonStatistics,
            imgButtonTraining,
            imgButtonGivingFeedback;
    ImageButton signInGoogle;
    Button logout;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    private long backPressedTime;
    private Toast backToast;
    private static final int EMAIL_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imgButtonLookupDictionary = findViewById(R.id.imgButtonLookupDictionary);
        imgButtonTraining = findViewById(R.id.imgButtonTraining);
        imgButtonStatistics = findViewById(R.id.imgButtonStatistics);
        imgButtonGivingFeedback = findViewById(R.id.imgButtonGivingFeedback);

        signInGoogle = findViewById(R.id.signInGoogle);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        logout = findViewById(R.id.btnLogout);

        imgButtonLookupDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDictionaryEntryDialog();
            }
        });

        imgButtonGivingFeedback = findViewById(R.id.imgButtonGivingFeedback);
        imgButtonGivingFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                /*.requestIdToken(getString(R.string.default_web_client_id))*/
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            String Name = account.getDisplayName();
            String Mail = account.getEmail();

            name.setText(Name);
            mail.setText(Mail);
            signInGoogle.setVisibility(View.GONE);
        } else {
            name.setText(null);
            mail.setText(null);

            signInGoogle.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            });
            logout.setVisibility(View.GONE);
        }

        logout.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });

        backPressedTime = System.currentTimeMillis();
    }

    private void sendFeedback() {
        String[] recipientEmails = {"dtc195480103dt0003@ictu.edu.vn"};
        String subject = "Phản hồi từ người dùng ứng dụng Ôn B1";
        String body = "Xin chào,\n\nTôi muốn gửi phản hồi về ứng dụng của bạn:\n";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recipientEmails);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, EMAIL_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Không tìm thấy ứng dụng email.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EMAIL_REQUEST_CODE) {

            Toast.makeText(this, "Gửi email thành công.", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Nhấn quay lại lần nữa để thoát ứng dụng", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    private void openDictionaryEntryDialog() {
        // Implement your logic for opening the dictionary entry dialog here
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SignOut();
            }
        });
        builder.setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void SignOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Toast.makeText(getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}