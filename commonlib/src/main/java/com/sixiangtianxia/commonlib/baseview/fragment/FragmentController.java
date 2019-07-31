package com.sixiangtianxia.commonlib.baseview.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.sixiangtianxia.commonlib.R;

import java.util.List;

/**
 * Creator: Gao MinMin.
 * Date: 2018/11/28.
 * Description: Fragment 控制器.
 */
public class FragmentController {

    private final FragmentManager mFragmentManager;

    public FragmentController(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void add(boolean isShow, String tag, int resourceId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (isFragmentExist(tag)) {
            fragment = getFragment(tag);
        } else {
            fragmentTransaction.add(resourceId, fragment, tag);
        }
        if (!isShow) {
            fragmentTransaction.hide(fragment);
        } else {
            fragment.setUserVisibleHint(true);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeFragment(String tag, int resourceId, Fragment targetFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (!tag.equals(fragment.getTag())) {
                    fragment.setUserVisibleHint(false);
                    fragmentTransaction.hide(fragment);
                } else {
                    targetFragment = fragment;
                }
            }
        }
        if (!isFragmentExist(tag)) {
            fragmentTransaction.add(resourceId, targetFragment, tag);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        targetFragment.setUserVisibleHint(true);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeFragment(String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.setCustomAnimations(R.animator.fragment_left_enter, R.animator.fragment_left_exit
//                , R.animator.fragment_right_enter, R.animator.fragment_right_exit);
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (!tag.equals(fragment.getTag())) {
                    fragmentTransaction.hide(fragment);
                } else {
                    fragment.setUserVisibleHint(true);
                    fragmentTransaction.show(fragment);
                }
            }
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeFragment(String tag, int resourceId, Fragment targetFragment, int HOMEPUSH) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (HOMEPUSH == 0) {
            fragmentTransaction.setCustomAnimations(R.anim.push_right_in,
                    R.anim.push_right_out, R.anim.push_right_in,
                    R.anim.push_right_out);

        } else {
            fragmentTransaction.setCustomAnimations(R.anim.push_left_in,
                    R.anim.push_left_out, R.anim.push_left_in,
                    R.anim.push_left_out);

        }
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (!tag.equals(fragment.getTag())) {
                    fragmentTransaction.hide(fragment);
                } else {
                    targetFragment = fragment;
                }
            }
        }
        if (!isFragmentExist(tag)) {
            fragmentTransaction.add(resourceId, targetFragment, tag);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        targetFragment.setUserVisibleHint(true);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeFragment(String tag, int HOMEPUSH) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (HOMEPUSH == 0) {
            fragmentTransaction.setCustomAnimations(R.anim.push_right_in,
                    R.anim.push_right_out, R.anim.push_right_in,
                    R.anim.push_right_out);

        } else {
            fragmentTransaction.setCustomAnimations(R.anim.push_left_in,
                    R.anim.push_left_out, R.anim.push_left_in,
                    R.anim.push_left_out);

        }
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (!tag.equals(fragment.getTag())) {
                    fragmentTransaction.hide(fragment);
                } else {
                    fragment.setUserVisibleHint(true);
                    fragmentTransaction.show(fragment);
                }
            }
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    public void hideFragment(String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.hide(getFragment(tag));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void hideFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


    public void showFragment(String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.show(getFragment(tag));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void removeFragment(String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.remove(getFragment(tag));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public boolean isFragmentExist(String tag) {
        boolean isExist = false;
        if (getFragment(tag) != null) {
            isExist = true;
        }
        return isExist;
    }

    public Fragment getFragment(String tag) {
        return mFragmentManager.findFragmentByTag(tag);
    }
}
