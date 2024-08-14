package com.github.clans.fab.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.fab.sample.R;

import java.util.ArrayList;
import java.util.List;

public class MenusFragment extends Fragment {

    private FloatingActionMenu menuRed;
    private FloatingActionMenu menuGreen;

    private FloatingActionButton fab2;

    private FloatingActionButton fabEdit;

    private final List<FloatingActionMenu> menus = new ArrayList<>();
    private final Handler mUiHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menus_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuRed = view.findViewById(R.id.menu_red);
        FloatingActionMenu menuYellow = view.findViewById(R.id.menu_yellow);
        menuGreen = view.findViewById(R.id.menu_green);
        FloatingActionMenu menuBlue = view.findViewById(R.id.menu_blue);
        FloatingActionMenu menuDown = view.findViewById(R.id.menu_down);
        FloatingActionMenu menuLabelsRight = view.findViewById(R.id.menu_labels_right);

        //Set content description programmatically
        menuDown.setContentDescription(getString(R.string.down_menu));

        //set menu label colors programmatically
        menuRed.setMenuLabelColorNormal(Color.GREEN);
        menuRed.setMenuButtonColorPressed(Color.GRAY);
        menuRed.setMenuLabelTextColor(Color.BLACK);

        FloatingActionButton fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        FloatingActionButton fab3 = view.findViewById(R.id.fab3);

        final FloatingActionButton programFab1 = new FloatingActionButton(getActivity());
        programFab1.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab1.setLabelText(getString(R.string.lorem_ipsum));
        programFab1.setImageResource(R.drawable.ic_add_photo);
        menuRed.addMenuButton(programFab1);
        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programFab1.setLabelColors(ContextCompat.getColor(requireActivity(), R.color.grey),
                        ContextCompat.getColor(requireActivity(), R.color.light_grey),
                        ContextCompat.getColor(requireActivity(), R.color.white_transparent));
                programFab1.setLabelTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
            }
        });

        ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), R.style.MenuButtonsStyle);
        FloatingActionButton programFab2 = new FloatingActionButton(context);
        programFab2.setLabelText("Programmatically added button");
        programFab2.setImageResource(R.drawable.ic_edit);
        menuYellow.addMenuButton(programFab2);

        fab1.setEnabled(false);
        menuRed.setClosedOnTouchOutside(true);
        menuBlue.setIconAnimated(false);

        menuDown.hideMenuButton(false);
        menuRed.hideMenuButton(false);
        menuYellow.hideMenuButton(false);
        menuGreen.hideMenuButton(false);
        menuBlue.hideMenuButton(false);
        menuLabelsRight.hideMenuButton(false);

        fabEdit = view.findViewById(R.id.fab_edit);
        fabEdit.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up));
        fabEdit.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down));

        menus.add(menuDown);
        menus.add(menuRed);
        menus.add(menuYellow);
        menus.add(menuGreen);
        menus.add(menuBlue);
        menus.add(menuLabelsRight);

        menuYellow.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                String text;
                if (opened) {
                    text = "Menu opened";
                } else {
                    text = "Menu closed";
                }
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabEdit.show(true);
            }
        }, delay + 150);

        menuRed.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuRed.isOpened()) {
                    Toast.makeText(getActivity(), menuRed.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                menuRed.toggle(true);
            }
        });

        createCustomAnimation();
    }


    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuGreen.getMenuIconView().setImageResource(menuGreen.isOpened()
                        ? R.drawable.ic_close : R.drawable.ic_star);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menuGreen.setIconToggleAnimatorSet(set);
    }

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:
                    break;
                case R.id.fab2:
                    fab2.setVisibility(View.GONE);
                    break;
                case R.id.fab3:
                    fab2.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
}
