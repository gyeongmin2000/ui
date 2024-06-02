package com.example.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendScreen(navController: NavController) {
    var selectedProjects by remember { mutableStateOf<MutableSet<Project>>(mutableSetOf()) }
    var isDeleteMode by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }

    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val db = AppDatabase.getInstance(LocalContext.current)

    LaunchedEffect(searchText) {
        withContext(Dispatchers.IO) {
            projects = if (searchText.isEmpty()) {
                db.projectDao().getAll()
            } else {
                db.projectDao().searchByName("%$searchText%")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        //삭제 모드일 경우
        if (isDeleteMode) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //삭제 모드 취소
                IconButton(onClick = { isDeleteMode = false }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }
//                Button(
//                    modifier = Modifier
//                        .padding(8.dp),
//                    onClick = { isDeleteMode = false }
//                ) {
//                    Text(text = "Cancel")
//                }

                //선택된 항목 삭제 버튼
                IconButton(onClick = {
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            selectedProjects.forEach { project ->
                                db.projectDao().delete(project)
                            }
                            projects = db.projectDao().getAll()
                            selectedProjects.clear()
                        }
                    }
                    isDeleteMode = false
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                }
//                Button(
//                    modifier = Modifier
//                        .padding(8.dp),
//                    onClick = {
//                        coroutineScope.launch {
//                            withContext(Dispatchers.IO) {
//                                selectedProjects.forEach { project ->
//                                    db.projectDao().delete(project)
//                                }
//                                projects = db.projectDao().getAll()
//                                selectedProjects.clear()
//                            }
//                        }
//                        isDeleteMode = false
//                    }
//                ) {
//                    Text(text = "Delete")
//                }
            }
        }

        // 검색 텍스트 필드
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Search") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(25.dp),
            trailingIcon = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            projects = if (searchText.isEmpty()) {
                                db.projectDao().getAll()
                            } else {
                                db.projectDao().searchByName("%$searchText%")
                            }
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Icon"
                    )
                }
            },
            keyboardOptions = KeyboardOptions( imeAction = ImeAction.Done ),
            singleLine = true,
            modifier = Modifier
                .background(
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(25.dp)
                )
                .fillMaxWidth()
        )

        //프로젝트 리스트
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(projects) { project ->
                ProjectItem(
                    project = project,
                    isDeleteMode = isDeleteMode,
                    isSelected = selectedProjects.contains(project),
                    onClick = {
                        navController.navigate("createProject?projectId=${project.projectId}")
                    },
                    onSelect = { isSelected ->
                        selectedProjects = if (isSelected) {
                            selectedProjects + project
                        } else {
                            selectedProjects - project
                        }.toMutableSet()
                    }
                )
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.0f))
        ,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        //플로팅 액션 버튼 누르면
        if (isExpanded) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .padding(20.dp)
            ) {
                //프로젝트 생성 버튼
                FabItem(
                    title = "Create",
                    icon = Icons.Filled.Create,
                    onClicked = {
                        isExpanded = !isExpanded
                        navController.navigate("createProject")
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

                //삭제 모드로 변경하는 버튼
                FabItem(
                    title = "Delete",
                    icon = Icons.Filled.Delete,
                    onClicked = {
                        isExpanded = !isExpanded
                        isDeleteMode = !isDeleteMode
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

        //플로팅 액션 버튼
        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.padding(16.dp),
            containerColor = colorResource(id = R.color.lightblue)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Button")
        }
    }
}

@Composable
fun FabItem(title: String, icon: ImageVector, onClicked: () -> Unit) {
    Row(modifier = Modifier.clickable {
        onClicked()
    }, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = "FabItem Icon"
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(title)
    }
}

@Composable
fun ProjectItem( project: Project, isDeleteMode: Boolean, isSelected: Boolean, onClick: () -> Unit, onSelect: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp, 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isDeleteMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = {
                        onSelect(it)
                    }
                )
            }
            Display(project = project)
        }
    }
}

@Composable
fun Display(project: Project) {
    Surface {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(5.dp)

        ) {
            Image(
                painter = rememberImagePainter(File(project.projectImagePath)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp))
            )
            
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = project.projectName,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}