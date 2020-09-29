// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.session.testing;

import static com.kelin.archetype.common.consants.Profile.PROFILE_TEST;

import com.kelin.archetype.api.session.SessionService;
import com.kelin.archetype.database.entity.biz.BizAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kelin Tan
 */
@Profile(PROFILE_TEST)
@Service
@Primary
@RequiredArgsConstructor
public class FakeSessionService implements SessionService {
    private BizAccount fakeAccount;

    @Override
    public BizAccount getCurrentAccount(HttpServletRequest request) {
        BizAccount account = null;
        if (fakeAccount != null) {
            account = new BizAccount();
            account.setAccount(fakeAccount.getAccount());
            account.setId(fakeAccount.getId());
        }

        restore();

        return account;
    }

    public void setFakeAccount(BizAccount fakeAccount) {
        this.fakeAccount = fakeAccount;
    }

    private void restore() {
        fakeAccount = new BizAccount();
        fakeAccount.setId(999L);
        fakeAccount.setAccount("Fake");
    }
}
