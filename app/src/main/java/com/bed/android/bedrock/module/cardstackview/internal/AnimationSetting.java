package com.bed.android.bedrock.module.cardstackview.internal;

import android.view.animation.Interpolator;

import com.bed.android.bedrock.module.cardstackview.Direction;

public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}
