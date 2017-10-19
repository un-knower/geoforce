package com.chaosting.geoforce.saas.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>Title:AreaLoadBalance</p>
 * <p>Description: </p>
 * 面相关dubbo负载均衡
 * <p>Company: 成都地图慧科技有限公司</p>
 *
 * @author zhouyun
 * @date 2017年7月5日11:27:47
 */
public class AreaLoadBalance extends AbstractLoadBalance {
    private final Random random = new Random();
    private static final String APPLICATION_VERSION = "application.version";
    private static final Logger LOGGER = LoggerFactory.getLogger(AreaLoadBalance.class);

    public AreaLoadBalance() {
    }

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        return invokers != null && invokers.size() != 0 ? this.doSelect(invokers, url, invocation) : null;
    }

    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {

        Object[] arguments = invocation.getArguments();
        String ak = null;
        if (null != arguments && arguments.length >= 1) {
            ak = (String) arguments[0];
        }

        List<Invoker<T>> newInvokers = null;
        if (!StringUtils.isEmpty(ak)) {
            newInvokers = new ArrayList<>();
            for (Invoker<T> invoker : invokers) {
                String akParam = invoker.getUrl().getParameter(APPLICATION_VERSION);
                if (ak.equals(akParam)) {
                    newInvokers.add(invoker);
                }
            }
        } else {
            return null;
        }

        if (!CollectionUtils.isEmpty(newInvokers)) {
            if (newInvokers.size() == 1) {
                return newInvokers.get(0);
            } else {
                int length = newInvokers.size();
                int totalWeight = 0;
                boolean sameWeight = true;

                int offset;
                int i;
                for (offset = 0; offset < length; ++offset) {
                    i = this.getWeight(newInvokers.get(offset), invocation);
                    totalWeight += i;
                    if (sameWeight && offset > 0 && i != this.getWeight(newInvokers.get(offset - 1), invocation)) {
                        sameWeight = false;
                    }
                }

                if (totalWeight > 0 && !sameWeight) {
                    offset = this.random.nextInt(totalWeight);

                    for (i = 0; i < length; ++i) {
                        offset -= this.getWeight(newInvokers.get(i), invocation);
                        if (offset < 0) {
                            return newInvokers.get(i);
                        }
                    }
                }

                return newInvokers.get(this.random.nextInt(length));
            }
        } else {
            return null;
        }
    }
}

