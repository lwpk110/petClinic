/**
 *
 * <h2>service 层</h2>
 *
 * <ul>
 *     <li>作为 “胶水层” 拼接各种逻辑代码</li>
 *     <li>接收 “controller” 层请求，处理逻辑并返回处理结果</li>
 * </ul>
 *
 * <h3> 规定</h3>
 * <ul>
 *	 	<li> "service" 禁止相互依赖 </li>
 * 		<li> "service" 层如果有共享的组件和逻辑，抽出共同的逻辑包装成一个 service 辅助组件，可以使用 “support”，“manager”等后缀命名 </li>
 * </ul>
 *
 */
package cn.tendata.samples.petclinic.service;
