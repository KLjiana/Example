package com.kaleblangley.examples.impl;

import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface TrailSaver {
    List<Vec3> getPastPositions();
}
