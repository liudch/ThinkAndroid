/*
 * Copyright (C) 2013  WhiteCat ��è (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ta.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @Title TAPrimaryKey ��������
 * @Package com.ta.core.util.db.annotation
 * @Description �����õ�ʱ��Ĭ�������id��_id�ֶ���Ϊ������column�����õ���Ĭ��Ϊ�ֶ���������������������Զ�����ID
 * @author ��è
 * @date 2013-1-20
 * @version V1.0
 */
@Target(
{ METHOD, FIELD })
@Retention(RUNTIME)
public @interface TAField
{
	/**
	 * �������õ���
	 * 
	 * @return
	 */
	public String name() default "";

	/**
	 * �������õ�Ĭ��ֵ
	 * 
	 * @return
	 */
	public String defaultValue() default "";

}