package com.iafenvoy.annotationlib.api;

import com.iafenvoy.annotationlib.util.IAnnotationLibEntryPoint;

/**
 * <p>Every class need to implement this interface when need to be used in Annotation Lib Command System.</p>
 * <p>You don't need to use this when you register your classes with {@link NetworkApi} <b>(Not Recommended)</b></p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
public interface IAnnotatedCommandEntry extends IAnnotationLibEntryPoint {
}
