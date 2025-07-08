package com.suvojeet.issuetracker.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val CRM_ID = stringPreferencesKey("crmId")
        val TL_NAME = stringPreferencesKey("tlName")
        val OTHER_TL_NAME = stringPreferencesKey("otherTlName")
        val ADVISOR_NAME = stringPreferencesKey("advisorName")
        val ORGANIZATION = stringPreferencesKey("organization")
        val INTERACTIVE_ONBOARDING_COMPLETE = booleanPreferencesKey("interactive_onboarding_complete")
        val ISSUE_HISTORY = stringSetPreferencesKey("issueHistory")
    }

    val crmId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.CRM_ID]
        }

    val tlName: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.TL_NAME]
        }

    val otherTlName: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.OTHER_TL_NAME]
        }

    val advisorName: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ADVISOR_NAME]
        }

    val organization: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ORGANIZATION]
        }

    val interactiveOnboardingComplete: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.INTERACTIVE_ONBOARDING_COMPLETE] ?: false
        }

    val issueHistory: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ISSUE_HISTORY] ?: emptySet()
        }

    suspend fun setCrmId(crmId: String) {
        context.dataStore.edit {
            it[PreferencesKeys.CRM_ID] = crmId
        }
    }

    suspend fun setTlName(tlName: String) {
        context.dataStore.edit {
            it[PreferencesKeys.TL_NAME] = tlName
        }
    }

    suspend fun setOtherTlName(otherTlName: String?) {
        context.dataStore.edit {
            if (otherTlName != null) {
                it[PreferencesKeys.OTHER_TL_NAME] = otherTlName
            } else {
                it.remove(PreferencesKeys.OTHER_TL_NAME)
            }
        }
    }

    suspend fun setAdvisorName(advisorName: String) {
        context.dataStore.edit {
            it[PreferencesKeys.ADVISOR_NAME] = advisorName
        }
    }

    suspend fun setOrganization(organization: String) {
        context.dataStore.edit {
            it[PreferencesKeys.ORGANIZATION] = organization
        }
    }

    suspend fun setInteractiveOnboardingComplete(complete: Boolean) {
        context.dataStore.edit {
            it[PreferencesKeys.INTERACTIVE_ONBOARDING_COMPLETE] = complete
        }
    }

    suspend fun addIssueToHistory(issueEntry: String) {
        context.dataStore.edit {
            val currentHistory = it[PreferencesKeys.ISSUE_HISTORY] ?: emptySet()
            it[PreferencesKeys.ISSUE_HISTORY] = currentHistory + issueEntry
        }
    }

    suspend fun clearIssueHistory() {
        context.dataStore.edit {
            it.remove(PreferencesKeys.ISSUE_HISTORY)
        }
    }
}