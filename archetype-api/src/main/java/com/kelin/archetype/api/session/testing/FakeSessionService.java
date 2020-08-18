// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.api.session.testing;

import static com.kelin.archetype.core.consants.Profile.PROFILE_TEST;

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
    private boolean mockUpdate = false;

    @Override
    public BizAccount getCurrentAccount(HttpServletRequest request) {
        if (mockUpdate) {
            if (fakeAccount == null) {
                clear();
                return null;
            } else {
                BizAccount account = new BizAccount();
                account.setId(fakeAccount.getId());
                account.setAccount(fakeAccount.getAccount());
                clear();
                return fakeAccount;
            }
        }
        if (fakeAccount == null) {
            clear();
        }
        return fakeAccount;
    }

    public void setFakeId(Long fakeId) {
        if (this.fakeAccount != null) {
            this.fakeAccount.setId(fakeId);
        }
        this.mockUpdate = true;
    }

    public void setFakeAccountName(String fakeAccountName) {
        if (this.fakeAccount != null) {
            this.fakeAccount.setAccount(fakeAccountName);
        }
        this.mockUpdate = true;
    }

    public void setFakeAccount(BizAccount fakeAccount) {
        this.fakeAccount = fakeAccount;
        this.mockUpdate = true;
    }

    private void clear() {
        fakeAccount = new BizAccount();
        fakeAccount.setId(999L);
        fakeAccount.setAccount("Fake");
    }
}
