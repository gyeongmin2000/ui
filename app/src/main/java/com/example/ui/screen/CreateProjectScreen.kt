package com.example.ui.screen


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ui.R
import com.example.ui.database.AppDatabase
import com.example.ui.database.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(navController: NavController, projectId: Long? = null) {
    var projectName by remember { mutableStateOf(TextFieldValue("")) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isEditMode by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = AppDatabase.getInstance(context)


    //RecommendScreen에서 item 클릭한 경우 해당 item 정보 저장
    if (projectId != null)
        LaunchedEffect(projectId) {
            withContext(Dispatchers.IO) {
                val project = db.projectDao().getById(projectId)
                projectName = TextFieldValue(project.projectName)
                selectedImageUri = Uri.fromFile(File(project.projectImagePath))
            }
        }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            //뒤로가기
            IconButton(
                onClick = {
                    navController.navigateUp()
                },
                modifier = Modifier
                    .size(50.dp)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            //이름
            TextField(
                value = projectName,
                onValueChange = { projectName = it },
                placeholder = { Text("Title") },
                enabled = isEditMode,
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions( imeAction = ImeAction.Done ),
                keyboardActions = KeyboardActions(
                    onDone = { isEditMode = false }
                ),
                singleLine = true,
                modifier = Modifier
                    .background(
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(25.dp)
                    )
            )

            //이름 수정 버튼
            IconButton(onClick = {
                isEditMode = !isEditMode
            }) {
                Icon(imageVector = if (isEditMode) Icons.Default.Done else Icons.Default.Edit, contentDescription = "icon")
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Card {
            //이미지 업로드
            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { imagePickerLauncher.launch("image/*") }
            ) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Upload")
            }

            //업로드한 이미지
            selectedImageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = "image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.padding(20.dp))
            }

            Row {
                //ai 결과
                //추천 가구 모델들, 카드 형식, 수평 스크롤
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))

        //저장
        ElevatedButton(
            onClick = {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        selectedImageUri?.let { uri ->
                            val inputStream = context.contentResolver.openInputStream(uri)
                            val file = File(context.filesDir, "${System.currentTimeMillis()}.jpg")
                            val outputStream = FileOutputStream(file)
                            inputStream?.copyTo(outputStream)
                            inputStream?.close()
                            outputStream.close()

                            val project = Project(
                                projectId = projectId ?: 0L,
                                projectName = projectName.text,
                                projectImagePath = file.absolutePath
                            )

                            if (projectId != null) {
                                db.projectDao().update(project)
                            } else {
                                db.projectDao().insert(project)
                            }

                        }
                    }
                    navController.navigateUp()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedImageUri != null && projectName.text.isNotBlank()
        ) {
            Text("Save")
        }
    }
}