package com.mikepenz.materialdrawer.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.iconics.IconicsColor.Companion.colorRes
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize.Companion.dp
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import kotlinx.android.synthetic.main.activity_sample_dark_toolbar.*

class DrawerActivity : AppCompatActivity() {

    //save our header or result
    private lateinit var headerResult: AccountHeader
    private lateinit var result: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_dark_toolbar)

        //Remove line to test RTL support
        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Handle Toolbar
        setSupportActionBar(toolbar)

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        val profile = ProfileDrawerItem().withName("Jaxson Aldrich").withEmail("jaxsonxaldrich@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(100)
        val profile2 = ProfileDrawerItem().withName("Zach Tsarnas").withEmail("tsarnas@github.com").withIcon("https://avatars2.githubusercontent.com/u/3597376?v=3&s=460").withIdentifier(101)
        val profile3 = ProfileDrawerItem().withName("Cyan Perez").withEmail("cyanxperez@gmail.com").withIcon(R.drawable.cyan).withIdentifier(102)
        val profile4 = ProfileDrawerItem().withName("Dylan Something").withEmail("ghost@gmail.com").withIcon(R.drawable.profile3).withIdentifier(103)
        val profile5 = ProfileDrawerItem().withName("RIP Midtown").withEmail("bacardiLover@gmail.com").withIcon(R.drawable.profile4).withIdentifier(104)
        val profile6 = ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5).withIdentifier(105)

        // Create the AccountHeader
        headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .addProfiles(
                        profile,
                        profile2,
                        profile3,
                        profile4,
                        profile5,
                        profile6,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().padding(dp(5)).color(colorRes(R.color.material_drawer_primary_text))).withIdentifier(PROFILE_SETTING.toLong()),
                        ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001)
                )
                .withOnAccountHeaderListener(object : AccountHeader.OnAccountHeaderListener {
                    override fun onProfileChanged(view: View?, profile: IProfile<*>, current: Boolean): Boolean {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile is IDrawerItem<*> && profile.identifier == PROFILE_SETTING.toLong()) {
                            val count = 100 + (headerResult.profiles?.size ?: 0) + 1
                            val newProfile = ProfileDrawerItem().withNameShown(true).withName("Batman$count").withEmail("batman$count@gmail.com").withIcon(R.drawable.profile5).withIdentifier(count.toLong())
                            headerResult.profiles?.let {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, it.size - 2)
                            } ?: headerResult.addProfiles(newProfile)
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build()

        //Create the drawer
        result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withItemAnimator(AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        PrimaryDrawerItem().withName(R.string.drawer_item_compact_header).withDescription(R.string.drawer_item_compact_header_desc).withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(1).withSelectable(false),
                        PrimaryDrawerItem().withName(R.string.drawer_item_action_bar_drawer).withDescription(R.string.drawer_item_action_bar_drawer_desc).withIcon(R.drawable.car).withIdentifier(2).withSelectable(false),
                        PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withDescription(R.string.drawer_item_multi_drawer_desc).withIcon(R.drawable.us).withIdentifier(3).withSelectable(false),
                        PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withDescription(R.string.drawer_item_non_translucent_status_drawer_desc).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4).withSelectable(false).withBadgeStyle(BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),
                        PrimaryDrawerItem().withName(R.string.drawer_item_advanced_drawer).withDescription(R.string.drawer_item_advanced_drawer_desc).withIcon(R.drawable.signin).withIdentifier(5).withSelectable(false),
                        
                        SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(20).withSelectable(false),
                        SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withIdentifier(21).withTag("Bullhorn")
                        /*,
                        DividerDrawerItem ()
                        SwitchDrawerItem ().withName("Switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                        SwitchDrawerItem ().withName("Switch2").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener).withSelectable(false)
                        ToggleDrawerItem ().withName("Toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                        DividerDrawerItem ()
                        SecondarySwitchDrawerItem ().withName("Secondary switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                        SecondarySwitchDrawerItem ().withName("Secondary Switch2").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener).withSelectable(false)
                        SecondaryToggleDrawerItem ().withName("Secondary toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                        */
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        var intent: Intent? = null
                        when {
                            drawerItem.identifier == 1L -> intent = Intent(this@DrawerActivity, CompactHeaderDrawerActivity::class.java)
                            drawerItem.identifier == 2L -> intent = Intent(this@DrawerActivity, ActionBarActivity::class.java)
                            drawerItem.identifier == 3L -> intent = Intent(this@DrawerActivity, MultiDrawerActivity::class.java)
                            drawerItem.identifier == 4L -> intent = Intent(this@DrawerActivity, NonTranslucentDrawerActivity::class.java)
                            drawerItem.identifier == 5L -> intent = Intent(this@DrawerActivity, AdvancedActivity::class.java)
                            drawerItem.identifier == 7L -> intent = Intent(this@DrawerActivity, EmbeddedDrawerActivity::class.java)
                            drawerItem.identifier == 8L -> intent = Intent(this@DrawerActivity, FullscreenDrawerActivity::class.java)
                            drawerItem.identifier == 9L -> intent = Intent(this@DrawerActivity, CustomContainerActivity::class.java)
                            drawerItem.identifier == 10L -> intent = Intent(this@DrawerActivity, MenuDrawerActivity::class.java)
                            drawerItem.identifier == 11L -> intent = Intent(this@DrawerActivity, MiniDrawerActivity::class.java)
                            drawerItem.identifier == 12L -> intent = Intent(this@DrawerActivity, FragmentActivity::class.java)
                            drawerItem.identifier == 13L -> intent = Intent(this@DrawerActivity, CollapsingToolbarActivity::class.java)
                            drawerItem.identifier == 14L -> intent = Intent(this@DrawerActivity, PersistentDrawerActivity::class.java)
                            drawerItem.identifier == 15L -> intent = Intent(this@DrawerActivity, CrossfadeDrawerLayoutActvitiy::class.java)
                            drawerItem.identifier == 1305L -> intent = Intent(this@DrawerActivity, NavControllerActivity::class.java)
                            drawerItem.identifier == 20L -> intent = LibsBuilder()
                                    .withFields(R.string::class.java.fields)
                                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                    .intent(this@DrawerActivity)
                        }
                        if (intent != null) {
                            this@DrawerActivity.startActivity(intent)
                        }

                        return false
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                //              .withShowDrawerUntilDraggedOpened(true)
                .build()

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(21, false)

            //set the active profile
            headerResult.activeProfile = profile3
        }

        result.updateBadge(4, StringHolder(10.toString() + ""))
    }

    /*
    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };
    */

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        if (::result.isInitialized) {
            outState = result.saveInstanceState(outState)
        }
        //add the values which need to be saved from the accountHeader to the bundle
        if (::headerResult.isInitialized) {
            outState = headerResult.saveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result.isDrawerOpen) {
            result.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }


    companion object {
        private const val PROFILE_SETTING = 100000
    }

}
