package com.github.wakingrufus.funk

import java.util.function.Supplier

/**
 * used for injecting in a Runtime configuration class that has been registered via {@link RuntimeConfigDsl}
 * @param <T>
 */

@FunctionalInterface
interface RuntimeConfig<T : Any> : Supplier<T>
