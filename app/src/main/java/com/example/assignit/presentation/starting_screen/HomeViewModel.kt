package com.example.assignit.presentation.starting_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignit.model.Group
import com.example.assignit.presentation.HOME_SCREEN
import com.example.assignit.presentation.LOGIN_SCREEN
import com.example.assignit.presentation.SPLASH_SCREEN
import com.example.assignit.repository.GroupRepository
import com.example.assignit.repository.UserRepository
import com.example.assignit.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
): ViewModel() {

    private val _currentGroupId = MutableLiveData<String>()
    val currentGroupId: LiveData<String> get() = _currentGroupId

    //TODO some more data loading later on...
    fun onAppStart(openAndPopUp: (String, String) -> Unit){
        if(userRepository.hasUser){
            openAndPopUp(HOME_SCREEN, SPLASH_SCREEN)
        } else {
            openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
        }
    }

    fun createGroup(name: String) {
        viewModelScope.launch {
            val firebaseUser = userRepository.currentUser.value ?: return@launch
            when (val userResource = userRepository.getUserDataById(firebaseUser.uid)) {
                is Resource.Success -> {
                    val user = userResource.data
                    val groupId = UUID.randomUUID().toString() // generate a unique ID for the group
                    val group = user?.let { Group(groupId, name, it) }
                    if (group != null) {
                        groupRepository.createGroup(group)
                        Log.d("HomeViewModel", "createGroup: $group $user $groupId")
                        //groupRepository.addUserToGroup(groupId, user)
                    }
                    _currentGroupId.value = groupId
                }
                is Resource.Error -> {
                    // Handle error when fetching user data
                }

                else -> {
                    // Handle loading
                }
            }
        }
    }


}