# Completed Tasks
We completed tasks 3.1, 3.2, 3.3, 3.4, and 3.7 for the final phase of this project.

## Task 3.1.
Added ability to create new organization by choosing option n, or logging in with existing organization by choosing option e. Added `/findOrgByLogin` (to check if org name already exists) and `/createOrg` api. Added `createOrg()` function to `UserInterface` and `getOrg()` which uses `/findOrgByLogin` api and `createOrg()` which uses `/createOrg` api to `DataManager`. These two methods are tested through `DataManager_createOrg_Test` and `DataManager_getOrg_Test` as well as additional tests in `DataManagerRobustnessTest`. We also added a new function `getLoginInfoFromArgs()` to `UserInterface` to maintain the functionality of using command line arguments for login.  

## Task 3.2. Organization App change password
Add ability to change password using the `p` option. Added `changePassword()` function to `UserInterface`, `checkPassword()` and `updatePassword()` to `DataManager`, and the `DataManager_checkPassword_Test` / `DataManager_updatePassword_Test` test files. Also added `/checkPassword` and `/updatePassword` api endpoints in `api.js`.

## Task 3.3. Organization App edit account information
Add ability to change name/description using the `u` option. Added `updateInfo()` function to `UserInterface`, `updateInfo()` to `DataManager`, and the `DataManager_updateInfo_Test` test file. Also added `/updateInfo` api endpoint to `api.js`.

## Task 3.4. Organization App make donation
Add the ability to enter a donation on behalf of a contributor to a particular fund. Added `attemptMakeDonation()` to `DataManager` and `DataManager_attemptMakeDonation_Test` test file.  Added `createDonation()` function to `UserInterface`.

## Task 3.7. Contributor App edit account information
Add ability to edit account information within the Contributor App, after the contributor logs in. Added `/findContributorPasswordById` and `/updateContributor` to `api.js`, and added methods `checkPassword` and `editUserUnfo` to `DataManager.java` respectively. The DataManager methods were tested using `DataManager_checkPassword_Test` and `DataManager_editUserInfo_Test` respectively.

For the user interface, added an Edit Info button in the Menu (in `activity_menu.xml`) and the `onEditInfoButtonClick` method in `MenuActivity`, which allows the user to begin the edit account information process. Password verification was implemented using a pop-up dialog from the Menu screen. Then created `activity_edit_info.xml` as a screen for the user to edit their information, as well as `EditInfoActivity.java` to send the information submitted through the corresponding screen and update the local copy of the contributor information accordingly.

# Known bugs / issues

# Contributions of team members
Lisa completed task 3.1, Vikram completed tasks 3.2 and 3.3, Michelle completed task 3.4, Grace completed task 3.7. All members contributed to the writeup, focusing on the features they implemented.
