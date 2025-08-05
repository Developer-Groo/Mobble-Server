    private final ClubRepository clubRepository;
    private final ClubCategoryRepository clubCategoryRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubAgeGroupRepository clubAgeGroupRepository;
    private final ClubValidator clubValidator;
    private final MemberValidator memberValidator;
    @Transactional
    public void createClub(Long memberId, ClubRequestDto dto) {
        ClubCategory category = findCategoryOrThrow(dto.category());
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        Club club = dto.toEntity(category);
        clubRepository.save(club);

        ClubMember clubMember = ClubMember.createClubMember(member, club, ClubMemberRole.LEADER, true);
        clubMemberRepository.save(clubMember);

        List<ClubAgeGroup> ageGroups = createClubAgeGroups(club, dto.ageGroup());
        clubAgeGroupRepository.saveAll(ageGroups);
    }

    public ClubResponseDto findClubById(Long clubId, Long memberId) {
        Club club = clubValidator.findClubByClubIdOrThrow(clubId);
        ClubMember leader = clubMemberRepository
                .findByClubIdAndClubMemberRole(clubId, ClubMemberRole.LEADER).get();
        String leaderName = leader.getMember().getName();

        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        // todo: 로그인 한 사용자가 가입안됬을때 공개여부 처리 질문

        return buildClubResponse(club, member, leaderName);
    }
    private ClubCategory findCategoryOrThrow(String categoryName) {
        return clubCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new DomainException(ClubErrorCode.CATEGORY_NOT_FOUND));
    }

    private ClubResponseDto buildClubResponse(Club club, Member member, String leaderName) {
        List<ClubAgeGroupType> ageGroupList = clubAgeGroupRepository.findByClub(club).stream()
                .map(ClubAgeGroup::getAgeGroupType)
                .toList();

        ClubLikeInfoDto likeInfo = clubRepository.findLikeInfoByClubIdAndMemberId(club.getId(), member.getId());

        return ClubResponseDto.toDto(club, leaderName, ageGroupList, likeInfo);
    }
    private List<ClubAgeGroup> createClubAgeGroups(Club club, List<ClubAgeGroupType> ageGroupTypes) {
        return ageGroupTypes.stream()
                .map(age -> ClubAgeGroup.createClubAgeGroup(club, age))
                .toList();
    }
