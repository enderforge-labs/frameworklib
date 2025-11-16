package com.snek.frameworklib.configs;

import com.snek.frameworkconfig.ConfigFile;
import com.snek.frameworkconfig.fields.ValueConfigField;








public class PerformanceConfig implements ConfigFile {

    //FIXME only keep one of this config. Either remove this one or its copy in shops mod
    public final ValueConfigField<Float> ray_casting_step = new ValueConfigField<>(
        new String[] {
            "The distance between ray casting steps. Measured in Blocks.",
            "Must be between 0.02 and 0.5.",
            "Higher values provide higher accuracy and responsiveness but can easily degrade performance."
        },
        0.2f
    );

    //FIXME only keep one of this config. Either remove this one or its copy in shops mod
    public final ValueConfigField<Integer> ray_casting_batches = new ValueConfigField<>(
        new String[] {
            "The amount of batches needed to complete a ray casting check cycle.",
            "Must be >= 1",
            "Player ray casting checks are performed every tick, but they can be split into batches to improve performance.",
            "e.g. 1 batch means that every player is checked every tick. 2 batches means that half the players are checked every even tick, while the other half is checked every odd tick.",
            "Higher values improve performance but increase shop hover detection delay by up to (ray_casting_batches + 1) ticks."
        },
        4
    );
    public final ValueConfigField<Integer> animation_refresh_time = new ValueConfigField<>(
        new String[] {
            "The time between transition updates. Measured in ticks.",
            "Must be between 1 and 10.",
            "Lower values create sharper and more accurate animations but increase server load and client lag."
        },
        2
    );








    @Override
    public void validate() {

        // Check ray casting step size
        if(ray_casting_step.getValue() < 0.02) throw new IllegalStateException("Ray casting step size must be >= 0.02");
        if(ray_casting_step.getValue() > 0.5)  throw new IllegalStateException("Ray casting step size must be <= 0.5");


        // Check ray casting batch size
        if(ray_casting_batches.getValue() < 1) throw new IllegalStateException("Ray casting batches must be >= 1");


        // Check animation refresh time
        if(animation_refresh_time.getValue() < 1)  throw new IllegalStateException("Animation refresh time must be >= 1");
        if(animation_refresh_time.getValue() > 10) throw new IllegalStateException("Animation refresh time must be <= 10");

    }
}
